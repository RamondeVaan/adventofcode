package nl.ramondevaan.adventofcode.day18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Day18 {
    private final List<Instruction> input;

    private Day18(List<Instruction> input) {
        this.input = input == null ?
                Collections.emptyList() :
                Collections.unmodifiableList(input);
    }

    private Map<Identifier, Register> getRegisters() {
        return input
                .stream()
                .flatMap(i -> i.getRegisterIds().stream())
                .filter(Objects::nonNull)
                .distinct()
                .sorted(Comparator.comparing(Identifier::getId, String::compareTo))
                .map(i -> new Register(i, 0))
                .collect(Collectors.toMap(Register::getId, i -> i));
    }

    public long recoveredFrequency() {
        SoundState state = new SoundState();
        state.setRegisters(getRegisters().values());

        InstructionState instructionState = new InstructionState(input);

        while (instructionState.hasNext() && !state.isRecoverCalled()) {
            instructionState.next().execute(state, instructionState);
        }

        return state.getLastFrequency();
    }

    public int messagesSent() {
        StateIdentifier s0 = new StateIdentifier(0);
        StateIdentifier s1 = new StateIdentifier(1);
        MessageQueue messageQueue = new MessageQueue(Arrays.asList(s0, s1));

        ProperState p0 = new ProperState(
                s0,
                messageQueue
        );
        ProperState p1 = new ProperState(
                s1,
                messageQueue
        );
        Map<Identifier, Register> g0 = getRegisters();
        Map<Identifier, Register> g1 = getRegisters();
        Identifier id = new Identifier("p");
        g0.put(id, new Register(id, 0));
        g1.put(id, new Register(id, 1));
        p0.setRegisters(g0.values());
        p1.setRegisters(g1.values());

        InstructionState i0 = new InstructionState(input);
        InstructionState i1 = new InstructionState(input);

        boolean b0 = !p0.isBlocking() && i0.hasNext();
        boolean b1 = !p1.isBlocking() && i1.hasNext();

        while(b0 || b1) {
            while(b0) {
                i0.next().execute(p0, i0);
                b0 = !p0.isBlocking() && i0.hasNext();
                b1 = !p1.isBlocking() && i1.hasNext();
            }

            while(b1) {
                i1.next().execute(p1, i1);
                b0 = !p0.isBlocking() && i0.hasNext();
                b1 = !p1.isBlocking() && i1.hasNext();
            }
        }

        return p1.getMessageCount();
    }

    public static Day18 create(Path file) throws IOException {
        InstructionFactory factory = new InstructionFactory();

        return new Day18(
                Files.lines(file)
                        .map(factory::parse)
                        .collect(Collectors.toList())
        );
    }

    public static Day18 create(List<Instruction> input) {
        return new Day18(new ArrayList<>(input));
    }
}
