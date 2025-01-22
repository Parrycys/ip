public class Storage extends Doobert{

    private String[] listOfItems = new String[100];
    private int currentIndex = 0;

    public void store(String input) {
        StringBuilder sb = new StringBuilder();
        if (currentIndex < listOfItems.length) {
            listOfItems[currentIndex] = input;
            currentIndex++;
            System.out.println("   ____________________________________________________________\n" +
                    "   added: " + input + "\n" +
                    "   ____________________________________________________________\n");
        } else {
            System.out.println("List is full.");
        }
    }

    public String printList() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < currentIndex; i++) {
            sb.append("   ").append((i + 1)).append(". ").append(listOfItems[i]).append("\n");
        }
        return sb.toString();
    }



}
