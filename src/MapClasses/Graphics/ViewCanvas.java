package MapClasses.Graphics;

import MapClasses.Graphics.EventfulImageCanvas;
import MapClasses.Navigation;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class ViewCanvas extends EventfulImageCanvas implements MouseListener
{
    Navigation myNav;

    public ViewCanvas(int width, int height, Navigation myNav)
    {
        super(width, height);
        this.myNav = myNav;
    }

    public void resized()
    {
        draw();
    }


    void draw()
    {

        int width = getWidth();
        int height = getHeight();

        Graphics2D pen = getPen();
        clear();

        myNav.drawGraph(pen, width, height);
        display();
    }

    public void mousePressed(MouseEvent e) {
        myNav.addPoint(e.getX(), e.getY(), getHeight(), getWidth());
        draw();
    }
}
