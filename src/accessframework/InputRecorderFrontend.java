package accessframework;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class InputRecorderFrontend extends JFrame implements ActionListener {

    InputRecorder plugin;
    JButton start;
    JButton playback;
    JButton stop;
    JButton load, save, clear;
    JPanel buttons;

    public InputRecorderFrontend(InputRecorder p) {
        plugin = p;

        this.setLocation(400, 400);
        setSize(300, 300);
        start = new JButton("Start recording");
        start.addActionListener(this);
        stop = new JButton("Stop recording");
        stop.addActionListener(this);
        playback = new JButton("Playback Recording");
        playback.addActionListener(this);
        load = new JButton("Load a Recording");
        load.addActionListener(this);
        save = new JButton("Save a Recording");
        save.addActionListener(this);
        clear = new JButton("Clear Recording");
        clear.addActionListener(this);

        buttons = new JPanel();
        buttons.setLayout(new GridLayout(6, 1));

        buttons.add(start);
        buttons.add(stop);
        buttons.add(playback);
        buttons.add(load);
        buttons.add(save);
        buttons.add(clear);

        setTitle ("Recording Panel");
        add(buttons, BorderLayout.CENTER);

        stop.setEnabled(false);
    }

    String getFilename(boolean save) {
        JFileChooser fc = new JFileChooser();
        File f;
        int ret;

        if (save == false) {
            ret = fc.showOpenDialog(this);
        }
        else {
            ret = fc.showSaveDialog(this);
        }

        if (ret == JFileChooser.APPROVE_OPTION) {
            f = fc.getSelectedFile();
            return f.getAbsolutePath();
        }

        return null;

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start) {
            start.setEnabled(false);
            stop.setEnabled(true);
            plugin.clearData();
            plugin.setRecording(true);
        } else if (e.getSource() == stop) {
            start.setEnabled(true);
            stop.setEnabled(false);
            plugin.setRecording(false);
        } else if (e.getSource() == playback) {
            if (plugin.getRecording()) {
                JOptionPane.showMessageDialog (this, "You cannot playback while recording.");
                return;
            }
            plugin.playBack();
        } else if (e.getSource() == save) {
            if (plugin.getRecording()) {
                JOptionPane.showMessageDialog (this, "You cannot save while recording.");
                return;
            }
            plugin.saveData(getFilename(true));
        } else if (e.getSource() == load) {
            if (plugin.getRecording()) {
                JOptionPane.showMessageDialog (this, "You cannot load while recording.");
                return;
            }
            plugin.loadData(getFilename(false));
        }
        else if (e.getSource() == clear) {
            plugin.clearData();
        }
    }
}

