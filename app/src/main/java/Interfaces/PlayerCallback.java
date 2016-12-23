package Interfaces;

import java.util.ArrayList;

import Classes.Post;

/**
 * Created by YoAtom on 12/12/2016.
 */

public interface PlayerCallback {
    void playPressed(Classes.Post post, int position, ArrayList<Post> list);
}
