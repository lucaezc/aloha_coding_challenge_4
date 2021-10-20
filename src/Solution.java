import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Solution {
	
	public static final String CMD_QUIT = "quit";
	public static final String CMD_CURRENT_DIR = "pwd";
	public static final String CMD_LIST = "ls";
	public static final String CMD_MAKE_DIR = "mkdir";
	public static final String CMD_CHANGE_DIR = "cd";
	public static final String CMD_CREATE_FILE = "touch";

	public static int countWords(String sentence) { 
		if (sentence == null || sentence.isEmpty()) { 
			return 0; 
		} 
		StringTokenizer tokens = new StringTokenizer(sentence); 
		return tokens.countTokens(); 
	}
	
	public static void initializeFileSystem() {

	}
	
	public static class File {
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public File(String name) {
			super();
			this.name = name;
		}
	}
	
	public static class Directory extends File {
		private LinkedList<File> content;

		public Directory(String name, LinkedList<File> content) {
			super(name);
			this.content = content;
		}
		
		public Directory(String name) {
			super(name);
			this.content = new LinkedList<File>();
		}

		public LinkedList<File> getContent() {
			return content;
		}

		public void setContent(LinkedList<File> content) {
			this.content = content;
		}
	}
	
	public static class Command{
		private String cmd;
		private String param;
			
		public Command(String strUser) {
			if (countWords(strUser) > 1) {
				String[] cmdArr = strUser.split(" ");
				this.cmd = cmdArr[0];
				this.param = cmdArr[1];
			} else {
				this.cmd = strUser;
				this.param = null;
			}
		}

		public String getCmd() {
			return cmd;
		}

		public void setCmd(String cmd) {
			this.cmd = cmd;
		}

		public String getParam() {
			return param;
		}

		public void setParam(String param) {
			this.param = param;
		}
		
		
	}
	
	public static void main(String[] args) throws IOException {
		
		LinkedList<File> dContent = new LinkedList<File>();
		Directory currentDir = new Directory("/root", dContent);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String strCommand = br.readLine();

		Command userCmd = new Command(strCommand);

		while (!userCmd.cmd.equals(CMD_QUIT)) {
			switch(userCmd.getCmd()) {
				case CMD_CURRENT_DIR:
					System.out.println(currentDir.getName());
					break;
				case CMD_LIST:

					break;
				case CMD_MAKE_DIR:
					if (userCmd.getParam().length() <= 100) {
						Directory newDir = new Directory(userCmd.getParam());
						currentDir.getContent().add(newDir);
					} else {
						System.out.println("Character limit of 100 for the name of the directory");
					}
					break;
				case CMD_CHANGE_DIR:
					
					break;
				case CMD_CREATE_FILE:
					if (userCmd.getParam().length() <= 100) {
						File newFile = new File(userCmd.getParam());
						currentDir.getContent().add(newFile);
					} else {
						System.out.println("Character limit of 100 for the name of the directory");
					}					
					break;
				case CMD_QUIT:
					System.exit(0);
					break;
			}
			
			strCommand = br.readLine();
			userCmd = new Command(strCommand);
		}
				
	}

}
