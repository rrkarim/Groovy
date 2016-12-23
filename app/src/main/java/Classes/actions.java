package Classes;

/**
 * Created by YoAtom on 12/14/2016.
 */

public class Actions {
    int isLiked, isPosted;
    public  Actions(int isLiked, int isPosted) {
        this.isLiked = isLiked;
        this.isPosted = isPosted;
    }

    public void setLike()  {
        this.isLiked = 1;
    }

    public void setPost()  {
        this.isPosted = 1;
    }

    public int getIsLiked() {
        return isLiked;
    }

    public int getIsPosted() {
        return isPosted;
    }
}
