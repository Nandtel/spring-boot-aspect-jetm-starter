package com.example;

import com.example.timer.TimeDelta;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.stream.IntStream;

@SpringBootApplication
@EnableAspectJAutoProxy
public class AspectApplication implements CommandLineRunner {

	public void test() throws InterruptedException {
		Thread.sleep(3000);
	}

	public static void main(String... args) {
		SpringApplication.run(AspectApplication.class, args);
	}

	@Override
	@TimeDelta
	public void run(String... args) throws Exception {
		IntStream.range(0, 3)
				.forEach(i -> {
					try {
						test();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				});
	}
}
