package pl.edu.pja.tpo7.s26822tpo7;
import com.google.googlejavaformat.java.FormatterException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.File;
import java.time.*;
@Controller
public class CodeController {
    private final FormatService formatService;
    public CodeController(FormatService formatService)
    {
        this.formatService=formatService;
        File file = new File("backup.txt");
        if (file.exists())
            formatService.readFromBackUp();
        this.formatService.check();
    }
    @GetMapping("/newCode")
    public String newCode(Model model) {
        model.addAttribute("code", new Code());
        return "addCode";
    }


    @PostMapping("/saveCode")
    public String addCode(@RequestParam("date") String dateString, @RequestParam("time") String timeString,Code code,
                                @RequestParam(name = "save",defaultValue = "false") boolean save,Model model) {
        if(code.getBody().isEmpty())
            return "redirect:/nullCode";

        if((code.getId().isEmpty()||dateString.isEmpty()||timeString.isEmpty())&&save)
            return "redirect:/nullCode";

        try {
            String format=this.formatService.reformat(code.getBody());
            if(save)
            {
                code.setBody(format);
                String datetimeString = dateString + "T" + timeString;
                LocalDateTime time=LocalDateTime.parse(datetimeString);
                if(Duration.between(LocalDateTime.now(), time).getSeconds()<10|| Period.between(LocalDateTime.now().toLocalDate(), time.toLocalDate()).getDays()>90)
                    return "redirect:/daterange";
                code.setExp(time);
                if (formatService.saveCode(code))
                {
                    return "redirect:/code?id="+code.getId();
                }
                else
                {
                    return "redirect:/invalidID";
                }
            }
            else
            {
                model.addAttribute("format",format);
                model.addAttribute("code",new Code());
                return "addCode";
            }
        } catch (FormatterException e) {
            return "redirect:/formater";
        }
    }
    @GetMapping("/nullCode")
    public String nullCode(Model model) {
        model.addAttribute("msg","The body of the code is missing");
        model.addAttribute("name","Null code");
        return "exception";
    }
    @GetMapping("/daterange")
    public String daterange(Model model) {
        model.addAttribute("msg","The provided expiration date is out of range( 10s-90days)");
        model.addAttribute("name","Date out ouf range");
        return "exception";
    }
    @GetMapping("/invalidID")
    public String invalidID(Model model) {
        model.addAttribute("msg","The provided id is already reserved for another code");
        model.addAttribute("name","Invalid ID");
        return "exception";
    }
    @GetMapping("/formater")
    public String formater(Model model) {
        model.addAttribute("msg","Formatting failed due to incorrect input structure");
        model.addAttribute("name","Formatting fail");
        return "exception";
    }
    @GetMapping("/code")
    public String getCode(@RequestParam(name = "id",required = false) String id, Model model) {
            formatService.findById(id).ifPresent(code -> model.addAttribute("code",code));
        return "code";
    }
}

