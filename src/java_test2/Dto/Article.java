package java_test2.Dto;

public class Article {

	public int id;
	public String regDate;
	public String updateDate;
	public String title;
	public String body;
	public int hit;
	public int memberId;
	
	public Article(int id, int memberId, String regDate, String updateDate, String title, String body) {
		this(id, memberId, regDate, updateDate, title, body, 0);
	}
	
	public Article(int id, int memberId, String regDate, String updateDate, String title, String body, int hit) {
		this.id = id;
		this.memberId = memberId;
		this.regDate = regDate;
		this.updateDate = updateDate;
		this.title = title;
		this.body = body;
		this.hit = hit;
	}
	
	public void hitup() {
		this.hit++;
	}
	
}
