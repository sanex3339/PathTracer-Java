package PathTracer;

import javax.swing.*;
import java.awt.*;

public class SaveMenu extends JPopupMenu {
    private JMenuItem saveImageItem = new JMenuItem("Save image");

    SaveMenu (String menuName) {
        super(menuName);
    }

    public void initMenu () {
        this.add(this.saveImageItem);
    }

    /**
     * @return JMenuItem
     */
    public JMenuItem getSaveImageItem () {
        return this.saveImageItem;
    }

    /**
     * @param var1
     * @param var2
     * @param var3
     */
    @Override
    public void show (Component var1, int var2, int var3) {
        super.show(var1, var2, var3);
    }
}
