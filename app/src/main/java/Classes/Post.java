package Classes;

import android.graphics.Bitmap;

/**
 * Created by YoAtom on 11/5/2016.
 */

public class Post {
    private int id, likesCount, typeId, countryId;
    private String title, headerImage, text;
    private Bitmap imageBitmap;

    public Post(int id, int likesCount, int typeId, int countryId, String title, String headerImage, String text) {
        this.id = id;
        this.likesCount = likesCount;
        this.typeId = typeId;
        this.countryId = countryId;
        this.title = title;
        this.headerImage = headerImage;
        this.text = text;

    }

    public int getId() {
        return id;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getTitle() {
        return title;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public String getText() {
        return text;
    }

    public void increaseCount() {
        this.likesCount++;
    }

    public boolean checkBitMap() {
        if(imageBitmap == null) return false;
        return true;
    }
    public Bitmap getImageBitmap() {
        return this.imageBitmap;
    }
    public void SetBitmap(Bitmap bitMap) {
        this.imageBitmap = bitMap;
    }
}
