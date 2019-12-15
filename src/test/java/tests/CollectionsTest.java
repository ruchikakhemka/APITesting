package tests;

import org.testng.annotations.Test;

import java.util.*;

public class CollectionsTest {

    @Test
    public void testArrayList(){
        List<String> topCompanies = new ArrayList<String>();
        //System.out.println(topCompanies.isEmpty());
        topCompanies.add("Google");
        topCompanies.add("Amazon");
        topCompanies.add("Apple");
        topCompanies.add("Tesla");
        //System.out.println(topCompanies.isEmpty());
        for(String s: topCompanies){
            System.out.println(s);
        }
        Iterator<String> myiterator = topCompanies.iterator();
        while (myiterator.hasNext())
        {
            System.out.println(myiterator.next());
        }
    }

    @Test
    public void testLinkedList()
    {
        List<String> travel_places = new LinkedList<String>();
        travel_places.add("London");
        travel_places.add("America");
        travel_places.add("Switzerland");
        travel_places.add("Germany");

        Iterator<String> travel_iterator = travel_places.iterator();
        while (travel_iterator.hasNext())
        {
            System.out.println(travel_iterator.next());
        }
    }

}
