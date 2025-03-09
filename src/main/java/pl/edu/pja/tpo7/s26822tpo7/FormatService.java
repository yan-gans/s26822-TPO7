package pl.edu.pja.tpo7.s26822tpo7;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FormatService {
    private  Formatter formatter=new Formatter();
    private  Map<String,Code> codes=new ConcurrentHashMap<>();
    public void readFromBackUp()
    {
        try {
            FileInputStream fileInputStream = new FileInputStream("backup.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            while (fileInputStream.available()>0)
            {
                Code code=(Code) objectInputStream.readObject();
                this.codes.put(code.getId(),code);
            }
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
e.printStackTrace();
        }
    }
    public void check()
    {
        Thread thread=new Thread(() -> {
            while (true)
            {

                for(Code code:codes.values())
                {
                    if(code!=null)
                    {
                        if(LocalDateTime.now().isAfter(code.getExp()))
                        {
                            codes.remove(code.getId());
                            backUp();
                        }
                    }

                }
            }
        });
        thread.start();
    }
    public void backUp()
    {
        try {FileOutputStream fileOutputStream = new FileOutputStream("backup.txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        for(Code code:this.codes.values())
        {
                objectOutputStream.writeObject(code);
        }
        if(this.codes.isEmpty())
        {
            File file=new File("backup.txt");
            file.delete();
        }
        objectOutputStream.flush();
        objectOutputStream.close();
        fileOutputStream.flush();
        fileOutputStream.close();
        } catch (IOException e) {
        e.printStackTrace();
    }
    }
    public boolean saveCode(Code code) {
        String id=code.getId();
        if(codes.containsKey(id))
            return false;
        codes.put(id,code);
        backUp();
        return true;
    }
    public Optional<Code> findById(String id){
        return Optional.ofNullable(codes.get(id));
    }
    public String reformat(String code) throws FormatterException {
        return this.formatter.formatSource(code);
    }
}
