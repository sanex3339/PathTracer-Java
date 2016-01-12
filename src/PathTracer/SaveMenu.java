package PathTracer;

import javax.swing.*;

public class SaveMenu extends JPopupMenu {
    private JMenuItem saveImageItem = new JMenuItem("Save image");

    SaveMenu (String menuName) {
        super(menuName);
    }

    public void initMenu () {
        this.add(this.saveImageItem);
    }

    public JMenuItem getSaveImageItem () {
        return this.saveImageItem;
    }
}
