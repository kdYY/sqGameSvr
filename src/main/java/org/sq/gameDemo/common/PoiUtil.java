package org.sq.gameDemo.common;

import org.apache.poi.hssf.usermodel.*;
import org.sq.gameDemo.svr.game.scene.model.GameScene;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class PoiUtil {

    public static List readExcel(File file, int sheetNum, Class clazz) throws Exception{
        //1.读取Excel文档对象
        int fieldNum = clazz.getDeclaredMethods().length;
        //将class的Field <name, setName()>记录到map中
        Map<String, Method> setMethodMap = new HashMap<>();//<id, setId()>
        for (Field field : clazz.getDeclaredFields()) {
            String name = field.getName();
            Method setMethod = clazz.getDeclaredMethod(
                    "set" + String.valueOf(name.charAt(0)).toUpperCase()+ field.getName().substring(1),
                    field.getType());

            setMethodMap.put(name, setMethod);
        }

        ArrayList excelData = new ArrayList<>();
        HSSFWorkbook hssfWorkbook = null;
        try {
            hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
            HSSFSheet sheet = hssfWorkbook.getSheetAt(sheetNum);
            int lastRowNum = sheet.getLastRowNum();
            Ref<List> listRef = new Ref<>();
            getSingleRow(sheet, 0, setMethodMap, listRef, clazz);//<1,name>
            for(int i = 1;i < lastRowNum; i++) {
                excelData.add(getSingleRow(sheet, i, setMethodMap, listRef, clazz));
            }
            return excelData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object getSingleRow(HSSFSheet sheet, int rowNum, Map<String, Method> setMethod, Ref<List> listRef, Class clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        List<Object> rowData = new ArrayList<>();//帶出引用
        Object instance = clazz.newInstance();
        HSSFRow row = sheet.getRow(rowNum);
        int lastCellNum = row.getLastCellNum() & '\uffff';
//                if(lastCellNum < fieldNum) {
//                    System.out.println("第" + i + "行數據缺失");
//                    throw new Exception(file.getName() + "文件有誤");
//                }
        for(int j = 0; j < lastCellNum; j++) {
            HSSFCell cell = row.getCell(j);
            Object value;
            if(rowNum == 0 && listRef.t == null) {
                value = getValueFromCell(cell, String.class);
                rowData.add(j, value);
            } else {
                //實例化bean

                Field declaredField = clazz.getDeclaredField(String.valueOf(listRef.t.get(j)));
                Class<?> type = declaredField.getType();
                value = getValueFromCell(cell, type);
                setMethod.get(listRef.t.get(j)).invoke(instance, value);
            }

        }
        if(rowNum == 0 && listRef.t == null) {
            listRef.t = rowData;
        }
        return instance;
    }

    /**
     * 获取单元格内容
     * @param cell
     * @return
     */
    private static Object getValueFromCell(HSSFCell cell, Class expectClazz) {
        Object value = null;
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                value = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    if (date != null) {
                        value = new SimpleDateFormat("yyyy-MM-dd")
                                .format(date);
                    } else {
                        value = "";
                    }
                } else {
//                    value = new DecimalFormat("0").format(cell
//                            .getNumericCellValue());
                    double cellValue = cell.getNumericCellValue();
                    value = new DecimalFormat("0").format(cellValue);
                    String typeName = expectClazz.getTypeName();
                    if(typeName.equals("int") || typeName.equals("Integer")) {
                        value = Integer.valueOf(new DecimalFormat("0").format(cellValue));
                    }
                    if(typeName.equals("double") || typeName.equals("Double")) {
                        value = cellValue;
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
                value = "";
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                value = (cell.getBooleanCellValue() == true ? true:false);
                break;
            default:
                value = "";
        }
        return value;
    }
    public static boolean isIntegerForDouble(double obj) {
        double eps = 1e-10;  // 精度范围
        return obj-Math.floor(obj) < eps;
    }

    public static void main(String[] args) {
        try {
            readExcel(new File("C:\\code\\sence.xls"), 0, GameScene.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
