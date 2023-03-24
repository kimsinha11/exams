package java_test2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java_test2.Dto.Article;
import java_test2.Dto.Member;
import java_test2.util.Util;

public class Main {
	static List<Article> articles = new ArrayList<>();
	static List<Member> members = new ArrayList<>();
	static Member loginedMember = null;

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		int lastArticleId = 3;
		int lastMemberId = 3;

		System.out.println("==프로그램 시작==");
		makeTestData();
		makeTestData2();

		while (true) {
			System.out.printf("명령어 > ");
			String cmd = sc.nextLine().trim();

			if (cmd.equals("exit")) {
				break;
			}
			if (cmd.equals("article write")) {
				if (isLogined() == true) {
					System.out.println("로그인 후 이용해주세요.");
					continue;
				}
				int id = lastArticleId + 1;
				System.out.printf("제목 : ");
				String title = sc.nextLine();
				System.out.printf("내용 : ");
				String body = sc.nextLine();
				String regDate = Util.getNowDateStr();
				String updateDate = "";

				Article article = new Article(id, loginedMember.id, regDate, updateDate, title, body);
				articles.add(article);

				System.out.printf("%d번글이 생성되었습니다.\n", id);
				lastArticleId++;

			} else if (cmd.equals("article list")) {
				String writer = null;
				if (articles.size() == 0) {
					System.out.println("게시물이 없습니다.");
					continue;
				} else {
					System.out.printf("번호 // 제목 // 조회 // 작성자 \n");
					for (int i = articles.size() - 1; i >= 0; i--) {
						Article article = articles.get(i);
						for(int j = 0; j<members.size(); j++) {
							Member member = members.get(j);
							if(member.id == article.memberId) {
								writer = member.name;
							}
						}
						System.out.printf("%d // %s // %d // %s \n", article.id, article.title, article.hit, writer);
					}
				}
			} else if (cmd.startsWith("article detail")) {
				String detail[] = cmd.split(" ");
				int id = Integer.parseInt(detail[2]);
				Article foundArticle = getArticleById(id);

				if (articles.size() == 0) {
					System.out.println("게시물이 없습니다.");
					continue;
				}

				String writer = null;

				for (int i = 0; i < members.size(); i++) {
					Member member = members.get(i);

					if (member.id == foundArticle.memberId) {
						writer = member.name;
					}
				}

				if (foundArticle == null) {
					System.out.printf("%d번 글은 존재하지 않습니다.\n", id);
					continue;
				} else {
					foundArticle.hitup();
					System.out.printf("번호 : %d\n작성날짜 : %s\n수정날짜 : %s\n작성자 : %s\n제목 : %s\n내용 : %s\n조회 : %d\n",
							foundArticle.id, foundArticle.regDate, foundArticle.updateDate, writer, foundArticle.title,
							foundArticle.body, foundArticle.hit);
				}
			} else if (cmd.startsWith("article delete")) {
				if (isLogined() == true) {
					System.out.println("로그인 후 이용해주세요.");
					continue;
				}
				String delete[] = cmd.split(" ");
				int id = Integer.parseInt(delete[2]);
				Article foundArticle = getArticleById(id);

				if (articles.size() == 0) {
					System.out.println("게시물이 없습니다.");
					continue;
				}

				if (foundArticle == null) {
					System.out.printf("%d번 글은 존재하지 않습니다.\n", id);
					continue;
				} else if (foundArticle.memberId == loginedMember.id) {
					articles.remove(foundArticle);
					System.out.printf("%d번 글을 삭제했습니다.\n", id);
				} else {
					System.out.println("권한이 없습니다");
					continue;
				}
			} else if (cmd.startsWith("article modify")) {
				if (isLogined() == true) {
					System.out.println("로그인 후 이용해주세요.");
					continue;
				}
				String modify[] = cmd.split(" ");
				int id = Integer.parseInt(modify[2]);
				Article foundArticle = getArticleById(id);

				if (articles.size() == 0) {
					System.out.println("게시물이 없습니다.");
					continue;
				}

				if (foundArticle == null) {
					System.out.printf("%d번 글은 존재하지 않습니다.\n", id);
					continue;
				} else if (foundArticle.memberId == loginedMember.id) {
					System.out.printf("제목 : ");
					String title = sc.nextLine();
					System.out.printf("내용 : ");
					String body = sc.nextLine();
					String updateDate = Util.getNowDateStr();

					foundArticle.title = title;
					foundArticle.body = body;
					foundArticle.updateDate = updateDate;

					System.out.printf("%d번 글을 수정했습니다.\n", id);
				} else {
					System.out.println("권한이 없습니다");
					continue;
				}
			} else if (cmd.equals("member join")) {
				if (isLogined() == false) {
					System.out.println("로그아웃 후 이용해주세요.");
					continue;
				}
				int id = lastMemberId + 1;
				String regDate = Util.getNowDateStr();
				String updateDate = "";
				String name = null;
				String loginId = null;
				String loginPw = null;
				String loginPwck = null;

				while (true) {
					System.out.printf("로그인 아이디 : ");
					loginId = sc.nextLine();

					if (getMemberById(loginId) != null) {
						System.out.println("이미 사용중인 아이디입니다");
						continue;
					}
					break;
				}

				while (true) {
					System.out.printf("로그인 비밀번호 : ");
					loginPw = sc.nextLine();
					System.out.printf("로그인 비밀번호 확인 : ");
					loginPwck = sc.nextLine();

					if (loginPw.equals(loginPwck) == false) {
						System.out.println("비밀번호를 확인해주세요");
						continue;
					}
					break;
				}

				System.out.printf("이름 : ");
				name = sc.nextLine();

				Member member = new Member(id, regDate, updateDate, loginId, loginPw, name);
				members.add(member);

				System.out.printf("%d번 회원이 가입되었습니다.\n", id);
				lastMemberId++;

			} else if (cmd.equals("member login")) {
				if (isLogined() == false) {
					System.out.println("로그아웃 후 이용해주세요.");
					continue;
				}
				String loginId = null;
				String loginPw = null;

				while (true) {
					System.out.printf("로그인 아이디 : ");
					loginId = sc.nextLine();

					if (getMemberById(loginId) == null) {
						System.out.println("없는 아이디입니다");
						continue;
					}
					break;
				}
				Member member = getMemberById(loginId);
				while (true) {
					System.out.printf("로그인 비밀번호 : ");
					loginPw = sc.nextLine();

					if (member.loginPw.equals(loginPw) == false) {
						System.out.println("비밀번호가 일치하지 않습니다");
						continue;
					} else {
						System.out.printf("로그인 성공! %s님 반갑습니다.\n", member.name);
						loginedMember = member;
					}
					break;
				}

			} else if (cmd.equals("member logout")) {
				if (isLogined() == true) {
					System.out.println("로그인 후 이용해주세요.");
					continue;
				} else {
					System.out.println("로그아웃 되었습니다");
					loginedMember = null;
				}

			}

			else {
				System.out.println("명령어가 없습니다.");
				continue;
			}
		}
		System.out.println("==프로그램 종료==");
		sc.close();

	}

	private static boolean isLogined() {
		if (loginedMember != null) {
			return false;
		}
		return true;
	}

	private static Member getMemberById(String loginId) {
		for (int i = 0; i < members.size(); i++) {
			Member member = members.get(i);

			if (member.loginId.equals(loginId)) {
				return members.get(i);
			}
		}
		return null;
	}

	private static Article getArticleById(int id) {
		for (int i = 0; i < articles.size(); i++) {
			Article article = articles.get(i);

			if (article.id == id) {
				return article;
			}
		}
		return null;
	}

	private static void makeTestData2() {
		System.out.println("==테스트를 위한 회원 데이터를 생성합니다==");
		members.add(new Member(1, Util.getNowDateStr(), "", "a", "a", "user1"));
		members.add(new Member(2, Util.getNowDateStr(), "", "b", "b", "user2"));
		members.add(new Member(3, Util.getNowDateStr(), "", "c", "c", "user3"));

	}

	private static void makeTestData() {
		System.out.println("==테스트를 위한 게시글 데이터를 생성합니다==");
		articles.add(new Article(1, 1,Util.getNowDateStr(), "", "제목1", "내용1", 11));
		articles.add(new Article(2, 2,Util.getNowDateStr(), "", "제목2", "내용2", 22));
		articles.add(new Article(3, 3,Util.getNowDateStr(), "", "제목3", "내용3", 33));
	}
}
