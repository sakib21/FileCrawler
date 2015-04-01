package lab6;

import java.util.*;
import java.io.*;

public class crawler {

	private WorkQueue workQ;
	static int i = 0;

	private class Worker implements Runnable {

		private WorkQueue queue;

		public Worker(WorkQueue q) {
			queue = q;
		}

		public void run() {
			String to_find = "f.txt";
			String name;
			while ((name = queue.remove()) != null) {
				File f = new File(name);
				String entries[] = f.list();
				if (entries == null)
					continue;
				for (String entry : entries) {
					if (entry.compareTo(".") == 0)
						continue;
					if (entry.compareTo("..") == 0)
						continue;
					to_find = to_find.toLowerCase();
					String temp = entry.toLowerCase();
					if (temp.compareTo(to_find) == 0){
						System.out.println("Found");
						String fn = name + "\\" + entry;
						System.out.println(fn);
					}
						
					
				}
			}
		}
	}

	public crawler() {
		workQ = new WorkQueue();
	}

	public Worker createWorker() {
		return new Worker(workQ);
	}


	public void processDirectory(String dir) {
		try{
			File f = new File(dir);
			if (f.isDirectory()) {
				String entries[] = f.list();
				if (entries != null)
					workQ.add(dir);

				for (String entry : entries) {
					String subdir;
					if (entry.compareTo(".") == 0)
						continue;
					if (entry.compareTo("..") == 0)
						continue;
					if (dir.endsWith("\\"))
						subdir = dir+entry;
					else
						subdir = dir+"\\"+entry;
					processDirectory(subdir);
				}
			}}catch(Exception e){}
	}

	public static void main(String Args[]) {

		crawler fc = new crawler();

		int N = 5;
		ArrayList<Thread> thread = new ArrayList<Thread>(N);
		for (int i = 0; i < N; i++) {
			Thread t = new Thread(fc.createWorker());
			thread.add(t);
			t.start();
		}

		fc.processDirectory("D://");

		fc.workQ.finish();

		for (int i = 0; i < N; i++){
			try {
				thread.get(i).join();
			} catch (Exception e) {};
		}
	}
}