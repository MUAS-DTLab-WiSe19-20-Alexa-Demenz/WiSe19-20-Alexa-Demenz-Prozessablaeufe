package guided.procedures.model;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class Procedure {
    @Getter
    private String name;
    private List<Step> steps;

    public Procedure(String name, List<Step> steps) {
        this.name = name;
        this.steps = stepListValidation(steps);
    }

    public boolean addStep(Step newStep) {
        if (steps.stream().anyMatch(step -> step.getName().equals(newStep.getName())) ||
                steps.stream().anyMatch(step -> step.getStepNr() == newStep.getStepNr())) {
            return false;
        } else {
            return this.steps.add(newStep);
        }
    }

    public Step getStep(int index) {
        log.debug("Procedure " + this.name + " getStep call with index " + index + "\n");
        return this.steps.get(index);
    }

    public int getAmountOfSteps() {
        return steps.size();
    }

    public boolean removeStep(String stepName) {
        return this.steps.removeIf(step -> step.getName().equals(stepName));
    }

    public boolean removeStep(int stepNr) {
        return this.steps.removeIf(step -> step.getStepNr() == stepNr);
    }

    //no two steps may have equal nr or name
    private List<Step> stepListValidation(List<Step> stepsToValidate) {
        stepsToValidate.forEach(step -> {
            if (stepsToValidate.stream().filter(step1 -> step1.getName().equals(step.getName())).count() > 1) {
                throw new IllegalArgumentException("Duplicate Step name: \"" + step.getName() + "\" detected, names must be unique to avoid confusion");
            }
        });
        stepsToValidate.forEach(step -> {
            if (stepsToValidate.stream().filter(step1 -> step1.getStepNr() == step.getStepNr()).count() > 1) {
                throw new IllegalArgumentException("Duplicate Step number: \"" + step.getStepNr() + "\" detected, numbers must be unique");
            }
        });
        return stepsToValidate;
    }
}
