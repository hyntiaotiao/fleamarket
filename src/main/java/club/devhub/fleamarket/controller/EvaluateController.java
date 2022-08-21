package club.devhub.fleamarket.controller;

import club.devhub.fleamarket.annotation.Idempotent;
import club.devhub.fleamarket.entity.User;
import club.devhub.fleamarket.param.EvaluateParam;
import club.devhub.fleamarket.service.EvaluationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api/fleamarket/v1/evaluate")
public class EvaluateController {

    @Autowired
    private EvaluationService evaluationService;

    @PostMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('USER')")
    public void evaluate(@PathVariable Long orderId, @RequestBody @Valid EvaluateParam evaluateParam, @AuthenticationPrincipal User user){
        evaluationService.evaluate(user.getUserId(),orderId,evaluateParam.getEvaluation());
    }

    @PutMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('USER')")
    public void edit(@PathVariable Long orderId, @RequestBody @Valid EvaluateParam evaluateParam, @AuthenticationPrincipal User user){
        evaluationService.edit(user.getUserId(),orderId,evaluateParam.getEvaluation());
    }
    @DeleteMapping("/{evaluateId}")
    @PreAuthorize("hasAnyRole('USER')")
    public void delete(@PathVariable Long evaluateId, @AuthenticationPrincipal User user){
        evaluationService.delete(user.getUserId(),evaluateId);
    }
}
