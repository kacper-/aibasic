package com.km.aibasic;

import com.km.aibasic.interpreter.InterpreterException;
import com.km.aibasic.interpreter.mem.Heap;
import com.km.aibasic.interpreter.mem.HeapListener;
import com.km.aibasic.interpreter.statement.Program;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main implements HeapListener {
    public static void main(String[] args) {
        Main main = new Main();
        main.run(args);
    }

    @Override
    public void listen(Heap heap) {
        System.out.println(heap.toString());
    }

    private void run(String[] args) {
        if (args.length == 1) {
            try {
                Program program = new Program(getCode(args[0]), Heap.getPrepared(this));
                program.execute();
            } catch (InterpreterException e) {
                System.out.println("Message : " + e.getMsg());
                System.out.println("Line    : " + e.getLine());
                System.out.println("Number  : " + e.getNumber());
            } catch (IOException e) {
                System.out.println("IO error : " + e.getMessage());
            }
            System.out.println("Finished");
        } else {
            System.out.println("Usage :");
            System.out.println("\tjava -jar aibasic.jar 'path_to_code'");
        }
    }

    private String getCode(String name) throws IOException {
        return Files.readString(Paths.get(name));
    }
}
