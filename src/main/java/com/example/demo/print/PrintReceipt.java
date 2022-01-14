package com.example.demo.print;

import com.example.demo.data.Order;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * @author REHAN JAVED
 * @version 1.0
 *
 *
 */
public class PrintReceipt implements Printable {

    // Attributes..
    private PrinterJob printerJob;
    private PageFormat pageFormat;
    private Paper paper;

    private double totalAmount = 0.0;
    private String orderid;
    private String cashAmount = "Ksh 0.0";
    private String cashBalance = "Ksh 0.0";

    private final int MARGIN = 1;

    private List<Order> listOrder;

    private BufferedImage buffImage;

    public PrintReceipt(List<Order> receiptList, double totalAmount, String orderid) {

        this.listOrder = receiptList;
        this.totalAmount = totalAmount;
        this.orderid = orderid;

        printerJob = PrinterJob.getPrinterJob();
        pageFormat = printerJob.defaultPage(); // Getting the page format.

        paper = new Paper(); // Create a new paper..

        int width = 216;
        int height = 1000;

        paper.setImageableArea(MARGIN, MARGIN, width, height);
        pageFormat.setPaper(paper);

        pageFormat.setOrientation(PageFormat.PORTRAIT);
        printerJob.setPrintable(this, pageFormat);

        try {

            buffImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/expresslogo.jpg")));

        } catch (IOException e){

                e.printStackTrace();
        }

        try {

            printerJob.print();

        } catch (PrinterException ex) {

            JOptionPane.showMessageDialog(null, "Printing Failed, Error: "+ex.toString());

        }

    }

    /**
     * Printing with Graphics..
     */
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {

        int result = NO_SUCH_PAGE;
        if (pageIndex == 0) {

            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());

            try{
                int y=20;
                int yShift = 10;
                int headerRectHeight=15;

                g2d.setFont(new Font("Monospaced",Font.PLAIN,8));
                g2d.drawImage(buffImage, 12, 0, 124, 60,null);y+=yShift+40;
                g2d.drawString("--------------------------",12,y);y+=yShift;
                g2d.drawString("      Express Laundry     ",12,y);y+=yShift;
                g2d.drawString("  Address: Ongata Rongai  ",12,y);y+=yShift;
                g2d.drawString("     Call: 0110 041 652   ",12,y);y+=yShift;
                g2d.drawString("        Instagram:        ",12,y);y+=yShift;
                g2d.drawString("   express_laundryRongai  ",12,y);y+=yShift;
                g2d.drawString("--------------------------",12,y);y+=headerRectHeight;
                g2d.drawString(" Order ID  -  "+orderid+" ",12,y);y+=headerRectHeight;
                g2d.drawString(" Desc          Qty   Price",10,y);y+=yShift;
                g2d.drawString("--------------------------",10,y);y+=headerRectHeight;

                for(int s = 0; s < listOrder.size(); s++)
                {
                    if(Objects.equals(listOrder.get(s).getName(), "homelaundry")){
                        if(listOrder.get(s).getQuantity() < 20){
                            g2d.drawString(" "+listOrder.get(s).getName()+"   "+listOrder.get(s).getQuantity()+"   "+listOrder.get(s).getTotal()+" ",10,y);
                        } else {
                            g2d.drawString(" " + listOrder.get(s).getName() + "   "+listOrder.get(s).getQuantity() +"   "+listOrder.get(s).getTotal()+" ", 10, y);
                        }
                        y+=yShift;
                    } else if(Objects.equals(listOrder.get(s).getName(), "jacketBlazer")){
                        g2d.drawString(" "+listOrder.get(s).getName()+"  "+listOrder.get(s).getQuantity()+"   "+listOrder.get(s).getTotal()+" ",10,y);y+=yShift;
                    } else if(Objects.equals(listOrder.get(s).getName(), "curtain")){
                        g2d.drawString(" "+listOrder.get(s).getName()+"       "+listOrder.get(s).getQuantity()+"   "+listOrder.get(s).getTotal()+" ",10,y);y+=yShift;
                    } else if(Objects.equals(listOrder.get(s).getName(), "duvet")){
                        g2d.drawString(" "+listOrder.get(s).getName()+"         "+listOrder.get(s).getQuantity()+"   "+listOrder.get(s).getTotal()+" ",10,y);y+=yShift;
                    } else {
                        g2d.drawString(" "+listOrder.get(s).getName()+"        "+listOrder.get(s).getQuantity()+"   "+listOrder.get(s).getTotal()+" ",10,y);y+=yShift;
                    }

                }

                g2d.drawString("--------------------------",10,y);y+=yShift;
                g2d.drawString(" Total   :     Ksh "+totalAmount+"",10,y);y+=yShift;
                g2d.drawString("--------------------------",10,y);y+=yShift;
                g2d.drawString(" Cash    :        "+cashAmount+"  ",10,y);y+=yShift;
                g2d.drawString("-------------------------",10,y);y+=yShift;
                g2d.drawString(" Balance :        "+cashBalance+" ",10,y);y+=yShift;
                g2d.drawString("*************************",10,y);y+=yShift;
                g2d.drawString("  THANK YOU COME AGAIN   ",10,y);y+=yShift;
                g2d.drawString("WE-WASH WE-DRY WE-DELIVER",10,y);y+=yShift;
                g2d.drawString("      Tukusafishie!      ",10,y);y+=yShift;
                g2d.drawString("*************************",10,y);y+=yShift;
                g2d.drawString(" SOFTWARE BY Toroyteach  ",10,y);y+=yShift;
                g2d.drawString(" bellenorthedynamics.com ",10,y);y+=yShift;
                g2d.drawString("-------------------------",12,y);y+=yShift;

                result = PAGE_EXISTS;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * Adding spaces into the num.
     * @param num spaces
     * @return all spaces in string.
     */
    public String spaces(int num) {

        String sp = "";
        for(int i = 0; i < num; i++)
            sp += " ";

        return sp;

    }

}