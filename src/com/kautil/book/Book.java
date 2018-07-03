package com.kautil.book;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Book {

    public static void main(String[] args){
        String url = "https://in.bookmyshow.com/buytickets/baahubali-2-the-conclusion-hyderabad/movie-hyd-ET00038693-MT/20170428";
        while(true){
            try{
                Connection conn = Jsoup.connect(url);
                if(conn!=null){
                    Document document = conn.get();
                    Element theatreList = document.body().getElementById("venuelist");
                    Elements theaters = theatreList.getElementsByTag("li");
                    for(Element theater:theaters){
                        Elements showsTimes = theater.getElementsByAttributeValue("data-online","Y");
                        for (Element show : showsTimes) {
                            Element aLink = show.getElementsByTag("a").get(0);
                            int showTime = Integer.parseInt(aLink.attr("data-showtime-code"));
                            String popup = aLink.attr("data-cat-popup");
                            int priceIndex = popup.indexOf("\"price\":\"")+"\"price\":\"".length();
                            float cost= Float.parseFloat(popup.substring(priceIndex,popup.indexOf('\"',priceIndex)));
                            if(showTime>=1800&&cost>100&&showTime<=2100){
                                boolean ticketsAvailable = areTicketsAvailable(theater);
                                String link = "https://in.bookmyshow.com"+aLink.attr("href");
                                if(ticketsAvailable) {
                                    String meridiem = ((showTime / 100) / 12) < 1 ? " AM" : " PM";
                                    String timeHuman = (showTime / 100) % 12 + ":"+showTime%100 + meridiem;
                                    System.out.println(String.format("Tickets available at %s %s", theater.attr("data-name"), timeHuman));
                                }
                            }
                        }
                    }
                }
                System.out.println("goto = " + url);
                System.out.println(" into sleep\n\n\n\n\n\n\n\n");
                Thread.sleep(30000);
            }catch (IOException|InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    private static boolean areTicketsAvailable(Element theater){
        long startTime = System.currentTimeMillis();
        String url = "https://in.bookmyshow.com/serv/doSecureTrans.bms";
        if(theater.attr("data-name").matches(".*((INOX)|(Carnival)).*")){
            return false;
        }
        return true;
    }

}
