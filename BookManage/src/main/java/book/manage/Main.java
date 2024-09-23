package book.manage;


import book.manage.entity.Book;
import book.manage.entity.Student;
import book.manage.sql.SqlUtil;
import lombok.extern.java.Log;
import org.apache.ibatis.io.Resources;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.LogManager;

@Log
public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            LogManager logManager = LogManager.getLogManager();
            logManager.readConfiguration(Resources.getResourceAsStream("logging.properties"));
            while (true) {
                System.out.println("===================");
                System.out.println("1.录入学生信息");
                System.out.println("2.录入书籍信息");
                System.out.println("3.添加借阅信息");
                System.out.println("4.查询借阅信息");
                System.out.println("5.查询学生信息");
                System.out.println("6.查询书籍信息");
                System.out.println("输入您想要执行的操作(输入其他任意键退出)");
                int input;
                try {
                    input =scanner.nextInt();
                }catch (Exception e) {
                    return;
                }
                scanner.nextLine();
                 switch (input) {
                    case 1:
                        addStudent(scanner);
                        break;
                        case 2:
                            addBook(scanner);
                            break;
                            case 3:
                                addBorrow(scanner);
                                break;
                                case 4:
                                    showBorrow();
                                    break;
                                    case 5:
                                        showStudent();
                                        break;
                                        case 6:
                                            showBook();
                                            break;

                     default:
                         return;
                 }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static void showStudent(){
        SqlUtil.doSqlWork(mapper -> {
            mapper.getStudentList().forEach(student -> {
                System.out.println(student.getSid()+"["+student.getName()+"]"+student.getSex()+"("+student.getGrade()+")");
            });
        });
    }
    public static void showBook(){
        SqlUtil.doSqlWork(mapper -> {
            mapper.getBookList().forEach(book -> {
                System.out.println(book.getBid()+"."+book.getTitle()+"["+ book.getPrice()+"]"+"("+book.getDesc()+")");
            });
        });
    }




    public static void showBorrow(){
        SqlUtil.doSqlWork(mapper -> {
            mapper.getBorrowList().forEach(borrow -> {
                System.out.println(borrow.getStudent().getName()+"->"+borrow.getBook().getTitle());
            });
        });
    }



    public static void addBorrow(Scanner scanner) {
        System.out.print("请输入书籍号");
        String a = scanner.nextLine();
        int bid = Integer.parseInt(a);
        System.out.println("请输入学号");
        String b = scanner.nextLine();
        int sid = Integer.parseInt(b);
        SqlUtil.doSqlWork(mapper -> {
            int i = mapper.addBorrow(sid, bid);
            if (i > 0) {
                System.out.println("录入借阅成功");
                log.info("添加一条借阅信息");
                log.info("借阅人学号："+sid+"借阅书籍号："+bid);
            }else System.out.println("录入失败");
        });
    }

    public static void addBook(Scanner scanner) {
        System.out.println("请输入书籍名：");
        String title = scanner.nextLine();
        System.out.println("请输入书籍介绍：");
        String desc = scanner.nextLine();
        System.out.println("请输入价格:");
        String price = scanner.nextLine();
        double p = Double.parseDouble(price);
        Book book = new Book(title, desc, p);
        SqlUtil.doSqlWork(mapper -> {
            int j = mapper.addBook(book);
            if (j > 0) {System.out.println("录入书籍成功");
            log.info("添加了一条书籍信息"+book);}
            else System.out.println("录入书籍失败");
        });
    }
    public static void addStudent(Scanner scanner) {
        System.out.print("请输入学生名字:");
       String name = scanner.nextLine();
        System.out.print("请输入学生的性别:");
        String sex = scanner.nextLine();
        System.out.println("请输入学生的年级：");
        String grade = scanner.nextLine();
        int g = Integer.parseInt(grade);
         Student student = new Student(name,sex,g);
        SqlUtil.doSqlWork(mapper -> {
           int i = mapper.addStudent(student);
            if (i>0) {
                System.out.println("录入成功");
                log.info("添加了一条学生信息"+student);
            }
            else System.out.println("录入失败");
        });
    }
}
