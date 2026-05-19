package ru.nsu.smolin.checker.dsl

import java.nio.file.Path

class CheckerScript extends Script {
    CheckerContext _ctx
    Path _scriptDir

    void tasks(Closure body) {
        TasksDsl.apply(_ctx, body)
    }

    void group(String name, Closure body) {
        GroupDsl.apply(_ctx, name, body)
    }

    void checkpoint(String name, Closure body) {
        CheckpointDsl.apply(_ctx, name, body)
    }

    void settings(Closure body) {
        SettingsDsl.apply(_ctx, body)
    }

    void assignment(Closure body) {
        AssignmentDsl.apply(_ctx, body)
    }

    void bonus(Closure body) {
        BonusDsl.apply(_ctx, body)
    }

    void apply(Map<String, String> args) {
        String from = args['from']
        if (!from) {
            throw new IllegalArgumentException("apply from: '<file>' required")
        }
        Path target = _scriptDir.resolve(from)
        ConfigLoader.executeInto(target, _ctx)
    }

    @Override
    Object run() {
        return null
    }
}
