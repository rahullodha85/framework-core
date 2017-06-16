package com.hbcd.execution.core;

import java.io.IOException;
import java.lang.Process;

public class CommandExecution {

    public void Execute(String[] commands) {
        try {
            // Execute a command with an argument that contains a space
            //String[] commands = new String[]{"grep", "hello world", "/tmp/f.txt"};

            //commands = new String[]{"grep", "hello world", "c:\\Documents and Settings\\f.txt"};

            Process child = Runtime.getRuntime().exec(commands);

        } catch (IOException e) {
        }
    }
}
