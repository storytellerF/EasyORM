package com.storyteller.gui.main;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class TerminalReader implements Runnable {
	private final InputStreamReader reader;
	private final Process process;
	private final PrintStream printStream;
	private final String tag;
	private final int millis;

	public TerminalReader(Process process, InputStreamReader reader,PrintStream printStream,String tag,int millis) {
		super();
		this.process = process;
		this.reader = reader;
		this.printStream=printStream;
		this.tag=tag;
		this.millis=millis;
	}

	@Override
	public void run() {
		System.out.println("run:tag:"+tag+" thread:" + Thread.currentThread().getId());
		while (true) {
			if (reader == null) {
				printStream.println("run:tag:"+tag+" 此时reader为空");
				break;
			} else {
				printStream.println("run:tag:"+tag+" execute read");
				char[] ch = new char[100];
				try {
					int countOnceRead;
					while ((countOnceRead = reader.read(ch)) != -1) {
						printStream.print(new String(ch, 0, countOnceRead));
					}
					if (!process.isAlive()) {// 进程结束
						printStream.println("run:tag:"+tag+" 进程结束，接收返回值");
						int i = process.exitValue();
						printStream.println("[PROCESS FINISHED]:tag:"+tag+":" + i+"\n");
						break;
					} else {
//						printStream.println("run:tag:"+tag+"sleep "+millis);
						Thread.sleep(millis);
					}
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		printStream.println("run:tag:"+tag+" thread stop:" + Thread.currentThread().getId());
	}

}
