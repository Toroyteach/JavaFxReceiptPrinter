package com.example.demo.print;

import com.example.demo.data.Order;
import com.example.demo.data.ReceiptData;
import javafx.scene.image.Image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

//    private double totalAmount = 0.0;
    private String cashAmount;
    private String cashBalance;

    private final int MARGIN = 1;

    private List<Order> listOrder;

    public PrintReceipt(List<Order> receiptList) {

        this.listOrder = receiptList;

        printerJob = PrinterJob.getPrinterJob();
        pageFormat = printerJob.defaultPage(); // Getting the page format.

        paper = new Paper(); // Create a new paper..

        // If you are working on printer rather than Thermal printers
        // then change the width and height accordingly.

        // I set them to 1000 value because that was for
        // receipt which will not be larger than 1000 points
        // actually this height does not mean the height of
        // paper get out from the printer, this is the height
        // of the printable area which you can use.
        int width = 216;
        int height = 1000;

        // width = totalWidthOfPage - (MARGIN * 2);
        // height = numberOfLines * 10 - (MARGIN * 2);

        paper.setImageableArea(MARGIN, MARGIN, width, height);
        pageFormat.setPaper(paper);

        pageFormat.setOrientation(PageFormat.PORTRAIT);
        printerJob.setPrintable(this, pageFormat);

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

            Image image = new Image("/expresslogo.jpg");
            ImageIcon icon=new ImageIcon(String.valueOf(image));
            Graphics2D g2d = (Graphics2D) graphics;
            double width = pageFormat.getImageableWidth();
            g2d.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());
            double totalAmount = 0.0;


            try{
                int y=20;
                int yShift = 10;
                int headerRectHeight=15;

                g2d.setFont(new Font("Monospaced",Font.PLAIN,9));
                //g2d.drawImage(icon.getImage(), 50, 20, 90, 30, 0);y+=yShift+30;
                g2d.drawString("-------------------------------------",12,y);y+=yShift;
                g2d.drawString("           Express Laundry           ",12,y);y+=yShift;
                g2d.drawString("       Address:  Ongata Rongai       ",12,y);y+=yShift;
                g2d.drawString("          Call: 0110 041 652         ",12,y);y+=yShift;
                g2d.drawString("             Instagram:              ",12,y);y+=yShift;
                g2d.drawString("        express_laundryRongai        ",12,y);y+=yShift;
                g2d.drawString("-------------------------------------",12,y);y+=headerRectHeight;

                g2d.drawString(" Qty     Desc                Price   ",10,y);y+=yShift;
                g2d.drawString("-------------------------------------",10,y);y+=headerRectHeight;

                for(int s = 0; s < listOrder.size(); s++)
                {
                    totalAmount += listOrder.get(s).getTotal();
                    if(Objects.equals(listOrder.get(s).getName(), "homelaundry")){
                        g2d.drawString("  "+listOrder.get(s).getQuantity()+"      "+listOrder.get(s).getName()+"        "+listOrder.get(s).getTotal()+" ",10,y);y+=yShift;
                    } else if(Objects.equals(listOrder.get(s).getName(), "jacketBlazer")){
                        g2d.drawString("  "+listOrder.get(s).getQuantity()+"      "+listOrder.get(s).getName()+"       "+listOrder.get(s).getTotal()+" ",10,y);y+=yShift;
                    } else if(Objects.equals(listOrder.get(s).getName(), "curtain")){
                        g2d.drawString("  "+listOrder.get(s).getQuantity()+"      "+listOrder.get(s).getName()+"            "+listOrder.get(s).getTotal()+" ",10,y);y+=yShift;
                    } else if(Objects.equals(listOrder.get(s).getName(), "duvet")){
                        g2d.drawString("  "+listOrder.get(s).getQuantity()+"      "+listOrder.get(s).getName()+"              "+listOrder.get(s).getTotal()+" ",10,y);y+=yShift;
                    } else {
                        g2d.drawString("  "+listOrder.get(s).getQuantity()+"      "+listOrder.get(s).getName()+"             "+listOrder.get(s).getTotal()+" ",10,y);y+=yShift;
                    }

                }

                g2d.drawString("-------------------------------------",10,y);y+=yShift;
                g2d.drawString(" Total amount:               "+totalAmount+"   ",10,y);y+=yShift;
                g2d.drawString("-------------------------------------",10,y);y+=yShift;
                g2d.drawString(" Cash      :                 "+cashAmount+"   ",10,y);y+=yShift;
                g2d.drawString("-------------------------------------",10,y);y+=yShift;
                g2d.drawString(" Balance   :                 "+cashBalance+"   ",10,y);y+=yShift;

                g2d.drawString("*************************************",10,y);y+=yShift;
                g2d.drawString("         THANK YOU COME AGAIN          ",10,y);y+=yShift;
                g2d.drawString("  WE WASH      WE DRY      WE DELIVER  ",10,y);y+=yShift;
                g2d.drawString("            Tukusafishie!              ",10,y);y+=yShift;
                g2d.drawString("*************************************",10,y);y+=yShift;
                g2d.drawString("       SOFTWARE BY Toroyteach          ",10,y);y+=yShift;
                g2d.drawString("CONTACT: tony@bellenorthedynamics.com  ",10,y);y+=yShift;

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