//import java.awt.*;
//import java.awt.event.*;
//import java.awt.image.*;
//
//class ViewCanvas extends EventfulImageCanvas implements MouseListener
//{
//    Navigator myNav;
//    BufferedImage myMapImage;
//
//    ViewCanvas(int width, int height, Navigator myNav)
//    {
//        super(width, height);
//        this.myNav = myNav;
//    }
//
//    public void resized()
//    {
//        draw();
//    }
//
//
//    void draw()
//    {
//
//        int width = getWidth();
//        int height = getHeight();
//
//        Graphics2D pen = getPen();
//        clear();
//
//        myNav.drawGraph(pen, width, height);
//        display();
//    }
//
//    public void mousePressed(MouseEvent e) {
//        myNav.addPoint(e.getX(), e.getY(), getPen());
//        draw();
//    }
//}
