package si.casino.card;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Card {

    private Suit suit;
    private Value value;

    private BufferedImage image;
    private Image image2;

    public Card(Value value, Suit suit) {
        this.value = value;
        this.suit = suit;

        try {
            String path = "gui/cards/";
            if (value.toString().startsWith("A") || value.toString().startsWith("J") ||
                    value.toString().startsWith("Q") || value.toString().startsWith("K")) {
                path += value.toString().charAt(0);
            } else {
                path += CardValue.getValue(this);
            }
            path += suit.toString().charAt(0) + ".png";
            image = ImageIO.read(new File(path));
            image2 = image.getScaledInstance(231, 352, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            System.out.println("ERROR LOADING IMAGE. " + e);
        }

    }

    @Override
    public String toString() {
        return this.value + " OF " + this.suit;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public Image getImage() {
        return image2;
    }
}
