/*
 * Данный класс осуществляет проверку являются ли слова
 * содержащиеся во входных данных палиндромами 
 */
public class Palindrome 
{
	 public static void main(String[] args) 
	 {
		 // Проверка являются ли слова из входных аргуемнтов палиндромами
		 // И вывод отчета по каждому слову
		 for (int i = 0; i < args.length; i++) 
		 {
			 	String s = args[i];
			 	System.out.println(s + " " + isPalindrome(s));
		 } 
	 }
	 public static String reverseString(String text)
	 // Метод возвращает перевернутую строку
	 {
		 String reverse = "";
		 for(int i = text.length()-1;i>=0;i--)
		 {
			 reverse+=text.charAt(i);
		 }
		 return reverse;
	 }
	 public static boolean isPalindrome(String s)
	 {
		 // Метод осуществляет проверку является ли строка палиндромом
		 String rs = reverseString(s);
		 return s.equals(rs);
	 }
}