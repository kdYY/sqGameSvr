package org.sq.gameDemo;

import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.sq.gameDemo.svr.common.GameSvr;

import java.net.InetSocketAddress;

@SpringBootApplication
public class SpringBootDemoApplication implements CommandLineRunner {


	@Value("${netty.port}")
	private int port;

	@Value("${netty.url}")
	private String url;

	@Autowired
	private GameSvr gameSvr;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDemoApplication.class, args);
	}

	@Override
	public void run(String... strings) {
		System.out.println("启动netty中");
		try {
			InetSocketAddress address = new InetSocketAddress(url, port);
			ChannelFuture future = null;
			future = gameSvr.run(address);
			Runtime.getRuntime().addShutdownHook(new Thread(){
				@Override
				public void run() {
					gameSvr.destroy();
				}
			});
			future.channel().closeFuture().syncUninterruptibly();
		}catch (Exception e) {
			gameSvr.destroy();
		}
		System.out.println("启动结束");
	}
}
