public class Main {
    public static void main(String[] args) {
        Integer var1 = Integer.parseInt(args[0]);
        Integer var2 = Integer.parseInt(args[1]);
        Integer ans = var1 + var2;
        System.out.println(String.format("%s", ans));
    }
}