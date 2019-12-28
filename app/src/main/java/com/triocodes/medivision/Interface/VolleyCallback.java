package com.triocodes.medivision.Interface;

import java.util.HashMap;

/**
 * Created by Hafi on 8/15/2015.
 */
public interface VolleyCallback {
    void volleyOnSuccess();
    void volleyOnError();
    HashMap<String, Object> getExtras();
}
