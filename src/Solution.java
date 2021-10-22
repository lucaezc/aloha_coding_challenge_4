import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Solution {
	
	public static final String CMD_QUIT = "quit";
	public static final String CMD_CURRENT_DIR = "pwd";
	public static final String CMD_LIST = "ls";
	public static final String CMD_MAKE_DIR = "mkdir";
	public static final String CMD_CHANGE_DIR = "cd";
	public static final String CMD_CHANGE_DIR_PARENT = "..";
	public static final String CMD_CREATE_FILE = "touch";
	public static final String ROOT = "/root";
	public static final String NO_PARAM = "*";

	public static int countWords(String sentence) { 
		if (sentence == null || sentence.isEmpty()) { 
			return 0; 
		} 
		StringTokenizer tokens = new StringTokenizer(sentence); 
		return tokens.countTokens(); 
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
		private Directory parent;

		public Directory getParent() {
			return parent;
		}

		public void setParent(Directory parent) {
			this.parent = parent;
		}

		public Directory(String name, LinkedList<File> content) {
			super(name);
			this.content = content;
		}
		
		public Directory(String name, LinkedList<File> content, Directory parent) {
			super(name);
			this.content = content;
			this.parent = parent;
		}
		
		public Directory(String name) {
			super(name);
			this.content = new LinkedList<File>();
		}
		
		public Directory(String name, Directory parent) {
			super(name);
			this.content = new LinkedList<File>();
			this.parent = parent;
		}

		public LinkedList<File> getContent() {
			return content;
		}

		public void setContent(LinkedList<File> content) {
			this.content = content;
		}
		
		public Boolean containsSubdirectory(String searchCriteria) {
			Boolean found = false;
			for (File f : this.getContent()) {
				if (f instanceof Directory) {
					if (f.getName().equals(searchCriteria)) {
						found = true;
						break;
					} 
				}
			}
			return found;
		}
		
		public Directory getSubdirectory(String searchCriteria) {
			Directory d = null;
			if (this.containsSubdirectory(searchCriteria)) {
				for (File f : this.getContent()) {
					if (f instanceof Directory) {
						if (f.getName().equals(searchCriteria)) {
							d = (Directory) f; 
							break;
						} 
					}
				}
			}
			return d;
		}
		
		public String getFullName() {
			String name = ROOT;
			if (!this.getName().equals(ROOT)) {
				name = this.parent.getFullName() + "/" + this.getName();
			}
			return name;
		}
		
		public ArrayList<String> listContent(){
			ArrayList<String> arr = new ArrayList<String>();
			for (File f : this.getContent()) {
			    arr.add(f.getName());
			}
			return arr;
		}
		
		public ArrayList<String> listContentRecursive(){
			ArrayList<String> arr = new ArrayList<String>();
			for (File f : this.getContent()) {
				arr.add(f.getName());
			}
			if (!this.getName().equals(ROOT)) {
				arr.addAll(this.parent.listContentRecursive());	
			}
			return arr;
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
				this.param = NO_PARAM;
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
		Directory currentDir = new Directory(ROOT, dContent);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String strCommand = br.readLine();

		Command userCmd = new Command(strCommand);

		while (!userCmd.cmd.equals(CMD_QUIT)) {
			switch(userCmd.getCmd()) {
				case CMD_CURRENT_DIR:
					System.out.println(currentDir.getFullName());
					break;
					
				case CMD_LIST:
					if (userCmd.getParam().equals(NO_PARAM)) {
						for (String s : currentDir.listContent()) {
						    System.out.println(s);
						}
					}else {
						if (userCmd.getParam().equals("-r")) {
							for (String s : currentDir.listContentRecursive()) {
							    System.out.println(s);
							}
						} else {
							System.out.println("Unrecognized parameter " + userCmd.getParam());
						}
					}
					break;
					
				case CMD_MAKE_DIR:
					if (userCmd.getParam().equals(NO_PARAM)) {
						System.out.println("Parameter must be specified while using " + CMD_MAKE_DIR + " command");
					} else {
						if (userCmd.getParam().length() <= 100) {
							Directory newDir = new Directory(userCmd.getParam(), currentDir);
							currentDir.getContent().add(newDir);
						} else {
							System.out.println("Character limit of 100 for the name of the directory");
						}
					}
					break;
					
				case CMD_CHANGE_DIR:
					if (userCmd.getParam().equals(NO_PARAM)) {
						System.out.println("Parameter must be specified while using " + CMD_CHANGE_DIR + " command");
					} else {
						if (userCmd.getParam().equals(CMD_CHANGE_DIR_PARENT)) {
							if (!currentDir.getName().equals(ROOT)) {
								currentDir = currentDir.getParent();
							}
						} else {
							if (currentDir.containsSubdirectory(userCmd.getParam())){
								currentDir = currentDir.getSubdirectory(userCmd.getParam());
							} else {
								System.out.println("Directory not found");
							}
						}
					}
					break;
					
				case CMD_CREATE_FILE:
					if (userCmd.getParam().equals(NO_PARAM)) {
						System.out.println("Parameter must be specified while using " + CMD_CREATE_FILE + " command");
					} else {
						if (userCmd.getParam().length() <= 100) {
							File newFile = new File(userCmd.getParam());
							currentDir.getContent().add(newFile);
						} else {
							System.out.println("Character limit of 100 for the name of the directory");
						}
					}
					break;
					
				case CMD_QUIT:
					System.exit(0);
					break;
					
				default:
					System.out.println("Command not found");
					break;
			}
			
			strCommand = br.readLine();
			userCmd = new Command(strCommand);
		}
				
	}

}
