package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class StudentDAO {
	
	//StudentDTO 객체를 관리할 배열 생성 
	private ArrayList<StudentDTO> students = new ArrayList<>();
	private StudentDTO sStudent;
	private int id = 1000;
	
	public StudentDAO() {
		//default state 유지 
	}
	
	//파일입출력 코드 
	//클라이언트한테 요청받은 파일을 (클라이언트 로컬 파일) 서버에 전송 
	//서버의 파일저장 경로는 임의로 "/Users/mir/Documents/project2/"로 설정 
	//byte배열에 담긴 내용 전달받은 후, 서버 물리저장소에 저장 
	public void uploadFile(DataInputStream dis, DataOutputStream dos) throws IOException {

		
		try {
			File file = new File("/Users/mir/Documents/project");
			

			// 파일 목록 전송
			File[] files = file.listFiles();
			System.out.println(files[0]);
			dos.writeInt(files.length);
			
			for (File f : files) {
				System.out.println(f.getName());
				dos.writeUTF(f.getName());
				dos.flush();
			}
			
			String fileName = dis.readUTF();
			System.out.println(fileName);
			
			File targetFile = new File("/Users/mir/Documents/project2/" + fileName );
			
			int fileSize = (int)targetFile.length();
			byte[] fileArr =  new byte[fileSize];
			
			dos.writeInt(fileSize);
			dos.flush();
			
			FileInputStream fis = new FileInputStream(targetFile); 
			DataInputStream fileDis = new DataInputStream(fis);
			
			fileDis.readFully(fileArr);
			dos.write(fileArr);
			dos.flush();

			fileDis.close();
			dos.close();
			

		}catch(Exception e) {
			System.err.println("print err");
		}

	}

	//검색할 학생의 ID를 받아 해당 요소 전부 반환 
	public void searchStudent(DataInputStream dis, DataOutputStream dos) throws IOException {

		int searchId = dis.readInt();
		
		try {

			for(StudentDTO student : students) {

				if(student.getId() == searchId) {
					
					dos.writeInt(student.getId());
					dos.flush();
					
					dos.writeUTF(student.getName());
					dos.flush();
					
					dos.writeInt(student.getKor());
					dos.flush();
					
					dos.writeInt(student.getEng());
					dos.flush();
					
					dos.writeInt(student.getMath());
					dos.flush();
					
					dos.writeInt(student.getSum());
					dos.flush();
					
					dos.writeDouble(student.getAvg());
					dos.flush();
					
					return;
				}
			}

		}catch(Exception e) {
			System.err.println("print err");
		}

	}
	
	
	//클라이언트가 수정하고 싶은 학생의 ID와 요소를 전달받아 set적용 
	public void editStudent( DataInputStream dis, DataOutputStream dos) throws IOException {

		int editId = dis.readInt();
		int editItem = dis.readInt();

		String editContent = dis.readUTF();

		for(int i=0;i<students.size();i++) {
			StudentDTO stud = students.get(i);
			if(stud.getId() == editId) {

				if(editItem == 1) {
					stud.setName(editContent);
				}
				else if(editItem == 2) {
					int score = Integer.parseInt(editContent);
					stud.setKor(score);
				}
				else if(editItem == 3) {
					int score = Integer.parseInt(editContent);
					stud.setEng(score);
				}
				else if(editItem == 4) {
					int score = Integer.parseInt(editContent);
					stud.setMath(score);
				}

				dos.writeBoolean(true);
				dos.flush();
				
				return;
			}

		}
		dos.writeBoolean(false);
		dos.flush();
		
	}
	
	
	//클라이언트로부터 삭제할 학생의 ID를 전달받아 해당 학생 삭제 
	public void deleteStudent(DataInputStream dis, DataOutputStream dos) throws IOException {

		int deleteId = dis.readInt();

		for(int i=0;i<students.size();i++) {
			if(students.get(i).getId() == deleteId) {
				students.remove(i);

				dos.writeBoolean(true);
				dos.flush();
				return;
			}
		}
		dos.writeBoolean(false);
		dos.flush();
		
	}


	//학생의 정보를 클라이언트에게 전달 
	public void getStudentInfo(DataInputStream dis, DataOutputStream dos) {

		try {
			if(students.isEmpty()) dos.writeBoolean(false);
			else dos.writeBoolean(true);
			dos.flush();

			dos.writeInt(students.size());
			dos.flush();

			for(StudentDTO student : students) {

				dos.writeInt(student.getId());
				dos.flush();

				dos.writeUTF(student.getName());
				dos.flush();

				dos.writeInt(student.getKor());
				dos.flush();

				dos.writeInt(student.getEng());
				dos.flush();

				dos.writeInt(student.getMath());
				dos.flush();

				dos.writeInt(student.getSum());
				dos.flush();

				dos.writeDouble(student.getAvg());
				dos.flush();
			}

		}catch(Exception e) {
			System.err.println("print err");
		}

	}
	
	
	//클라이언트가 입력한 학생의 정보를 받아 생성자 매개변수로 전달
	//DAO 배열에 추가하여 학생 DB관리 
	public void addStudent(DataInputStream dis, DataOutputStream dos) throws IOException {

		try {
			sStudent = new StudentDTO(id, dis.readUTF(), dis.readInt(), dis.readInt(), dis.readInt());
			students.add(sStudent);
			dos.writeBoolean(true);
			id++;
		}catch(Exception e) {
			dos.writeBoolean(false);
		}finally {
			dos.flush();
		}
	}


}
