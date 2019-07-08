package org.sq.gameDemo.svr.common;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.sq.gameDemo.common.proto.EntityProto;
import org.sq.gameDemo.svr.game.scene.model.GameScene;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class PoiUtil {
    /**
     * 主方法
     * @param fileName excel文件流
     * @param sheetNum 第sheetNum个表格
     * @param clazz 实体class
     * @return
     * @throws Exception
     */
    public static List readExcel(String fileName, int sheetNum, Class clazz) throws Exception{
        //1.读取Excel文档对象
        int fieldNum = clazz.getDeclaredMethods().length;
        //将class的Field的名称<name, setName()>记录到map中
        Map<String, Method> setMethodMap = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            String name = field.getName();
            try{
                Method setMethod = clazz.getDeclaredMethod(
                        "set" + String.valueOf(name.charAt(0)).toUpperCase()+ field.getName().substring(1),
                        field.getType());
                setMethodMap.put(name, setMethod);
            }catch (NoSuchMethodException e) {
                continue;
            }



        }

        ArrayList excelData = new ArrayList<>();
        HSSFWorkbook hssfWorkbook = null;
        try {
            File file = new File(fileName);
            InputStream resourceAsStream = PoiUtil.class.getClassLoader().getResourceAsStream(fileName);
            hssfWorkbook = new HSSFWorkbook(resourceAsStream);
            HSSFSheet sheet = hssfWorkbook.getSheetAt(sheetNum);
            int lastRowNum = sheet.getLastRowNum();
            //Ref为了将第一行标题取出来
            Ref<List> listRef = new Ref<>();
            getSingleRow(sheet, 0, setMethodMap, listRef, clazz);//<1,name>

            //将每一行的数据注入到class实例中并保存在list中
            for(int i = 1;i <= lastRowNum; i++) {
                try {
                    Object singleRow = getSingleRow(sheet, i, setMethodMap, listRef, clazz);
                    excelData.add(singleRow);
                }catch (Exception e) {
                    throw new Exception("该路径下 " + file.getPath() + " 文件内容有誤");
                }

            }
            return excelData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据传入的clazz 将每一行的数据填充到clazz.instance的实例中，然后返回
     * @param sheet
     * @param rowNum 取第rownum行
     * @param setMethod class中所有属性对应set属性方法的map映射；譬如class类中 public String name; 就会有-> <name, setName()>
     * @param listRef 引用，从参数取方法中的某个局部变量的引用
     * @param clazz 实例类变量
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchFieldException
     */
    private static Object getSingleRow(HSSFSheet sheet, int rowNum, Map<String, Method> setMethod, Ref<List> listRef, Class clazz) throws Exception {
        HSSFRow row = sheet.getRow(rowNum);

        if(!checkRow(row, rowNum, setMethod)) {
            System.out.println("err: 第" + (rowNum + 1) + "行數據有误");
            throw new Exception("PoiUtil -> excel文件有誤");
        }

        //此次读取是否为标题行读取
        boolean titleRow = (rowNum == 0 && listRef.ref == null);
        List<Object> rowData = new ArrayList<>();
        Object instance = clazz.newInstance();
        int lastCellNum = row.getLastCellNum() & '\uffff'; //盗poi的...

        for(int j = 0; j < lastCellNum; j++) {
            HSSFCell cell = row.getCell(j);
            Object value;
            if(titleRow) {
                value = getValueFromCell(cell, String.class);
                rowData.add(j, value);
            } else {
                //实例化Class对象，并注入数据
                Field declaredField = clazz.getDeclaredField(String.valueOf(listRef.ref.get(j)));
                Class<?> type = declaredField.getType();
                value = getValueFromCell(cell, type);
                setMethod.get(listRef.ref.get(j)).invoke(instance, value);
            }

        }
        if(titleRow) {
            listRef.ref = rowData;
        }
        return instance;
    }

    /**
     * 获取单元格内容
     * @param cell 单元格
     * @param expectClazz 期望的单元格内容的数据类型
     * @return
     */
    private static Object getValueFromCell(HSSFCell cell, Class expectClazz) {
        Object value = null;
        String typeName = expectClazz.getTypeName();

        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                value = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    if (date != null) {
                        if(typeName.equals("String")) {
                            value = new SimpleDateFormat("yyyy-MM-dd").format(date);
                        }
                        if(typeName.equals("Date")) {
                            value = date;
                        }
                    } else {
                        value = null;
                    }
                } else {
                    Double cellValue = cell.getNumericCellValue();
                    value = new DecimalFormat("0").format(cellValue);
                    if(typeName.equals("int") || typeName.equals("Integer")) {
                        value = Integer.valueOf(new DecimalFormat("0").format(cellValue));
                    }
                    if(typeName.equals("double") || typeName.equals("Double")) {
                        value = cellValue;
                    }
                    if(typeName.equals("float") || typeName.equals("Float")) {
                        value = cellValue.floatValue();
                    }
                }
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                // 导入时如果为公式生成的数据则无值
                if (!cell.getStringCellValue().equals("")) {
                    value = cell.getStringCellValue();
                } else {
                    value = cell.getNumericCellValue() + "";
                }
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                break;
            case HSSFCell.CELL_TYPE_ERROR:
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            default:
                value = null;
        }
        return value;
    }


    private static boolean checkRow(Row row, int rowNum, Map<String, Method> setMethod) {
        int lastCellNum = row.getLastCellNum() & '\uffff'; //盗poi的...
        if(lastCellNum != setMethod.keySet().size()) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        try {
            List<EntityProto.Entity> list = readExcel("C:\\code\\git\\src\\main\\resources\\entity.xls", 0, EntityProto.Entity.class);
            list.forEach(sence->{
                System.out.println(sence.getName());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
