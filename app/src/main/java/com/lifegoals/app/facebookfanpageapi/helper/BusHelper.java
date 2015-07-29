package com.lifegoals.app.facebookfanpageapi.helper;


import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by Paul on 4/12/2015.
 */
public class BusHelper {

    public static Bus bus = new Bus(ThreadEnforcer.MAIN);
}
