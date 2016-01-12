package PathTracer.interfaces;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@FunctionalInterface
public interface ClickListener extends MouseListener
{
    @Override
    default void mouseEntered(MouseEvent e) {}

    @Override
    default void mouseExited(MouseEvent e) {}

    @Override
    default void mouseClicked(MouseEvent e) {}

    @Override
    default void mouseReleased(MouseEvent e) {}
}
