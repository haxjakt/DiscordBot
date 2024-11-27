package net.haxjakt.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.haxjakt.core.ProgramRecord;
import net.haxjakt.core.ProgramRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/program")
public class TestController {
    final ProgramRepository repository;

    @GetMapping
    ResponseEntity<String> getJavaCode(@RequestParam("prog-name") String programName) {
        Optional<ProgramRecord> programOpt = repository.findByScriptName(programName);
        if (programOpt.isEmpty()) {
            return new ResponseEntity<>("The program was not found.", HttpStatus.NOT_FOUND);
        }
        ProgramRecord program = programOpt.get();
        return new ResponseEntity<>(program.getJavaSourceCode(), HttpStatus.OK);
    }



}
