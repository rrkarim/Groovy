package Interfaces;

import Classes.User;

/**
 * Created by YoAtom on 12/23/2016.
 */

public interface UserCallback {
    User getUser();
    void logoutAction();
}
