package com.ipmcpmjournal.journal.ipmcpm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class JournalIpmCpmApplication {

	public static void main(String[] args) {
		SpringApplication.run(JournalIpmCpmApplication.class, args);
	}




}
