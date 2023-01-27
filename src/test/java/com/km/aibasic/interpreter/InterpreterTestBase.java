package com.km.aibasic.interpreter;

import com.km.aibasic.interpreter.mem.Heap;
import com.km.aibasic.interpreter.mem.HeapListener;
import com.km.aibasic.interpreter.statement.Program;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class InterpreterTestBase implements HeapListener {
    private boolean useListener;

    protected Heap executeProgram(String fileName) throws Exception {
        return executeProgram(fileName, true);
    }

    protected Heap executeProgram(String fileName, boolean useListener) throws Exception {
        this.useListener = useListener;
        URL url = InterpreterTestBase.class.getResource("/" + getType() + "/" + fileName);
        String code = Files.readString(Path.of(url.toURI()));
        Heap heap = Heap.getPrepared(this);
        Program program = new Program(code, heap);
        program.execute();
        return heap;
    }

    protected abstract String getType();

    @Override
    public void listen(Heap heap) {
        if (useListener) {
            System.out.println(heap);
        }
    }
}