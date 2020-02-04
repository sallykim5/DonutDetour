import java.util.*;
import java.io.*;

public class ConvertFiles {

	public void read_file(String fileName, String fileName2) {
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(fileName2);
			BufferedWriter bw = new BufferedWriter(fw);
			boolean hasLine = true;
			String line = "";
			while(hasLine) {
				line = br.readLine();
				if(line == null) {
					hasLine = false;
					break;
				}
				if(line.length() > 0) {
					int index = 0;
					StringBuilder sb = new StringBuilder();
					for(int i = 0; i < line.length(); i++) {
						if(line.charAt(i) == ',') {
							if(index == 2) {
								sb.append(';');
							}
							else {
								index++;
								sb.append(line.charAt(i));
							}
						}
						else sb.append(line.charAt(i));
					}
					line = sb.toString();
					System.out.println(line);
					bw.write(line + "\n");
				}
			}
			bw.flush();
			fw.close();
			fr.close();
		}
		catch(IOException io) {
			System.out.println("Error: " + io);
		}
	}

	public static void main(String[] args) {
		test t = new test();
		t.read_file(args[0], args[1]);
	}
}