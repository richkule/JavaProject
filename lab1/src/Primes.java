public class Primes 
{
	 public static void main(String[] args) 
	 {
		 // Перебор чисел от 2 до 100 и вывод простых
		 for(int i = 2;i<=100;i++)
		 {
			 if (isPrime(i)) System.out.print(i + " ");
		 }
	 }
	 public static boolean isPrime(int n)
	 // Функция проверки простоты числа
	 {
		 for(int i = 2;i<n;i++)
		 {
			 if (n%i==0) return false;
		 }
		 return true;
	 }
}
