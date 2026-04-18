# BehaviorAI Changelog

## 1.0.0-SNAPSHOT — Initial release

### Added

- **Behavior-tree runtime** (`tree/`): `BehaviorStatus` (SUCCESS/FAILURE/RUNNING),
  `BehaviorNode`, `BehaviorContext`, composites (`SelectorNode`, `SequenceNode`,
  `ParallelNode`), decorators (`InverterNode`, `RepeatNode`), leaf bases
  (`ConditionNode`, `ActionNode`). Pure Java, no engine dependency — usable in
  unit tests without the ECS harness.
- **ECS components** (`components/`): `AIComponent` (enable flag + tick
  divisor), `GoalComponent` (prioritized goal list + active-goal tracking),
  `MemoryComponent` (perception blackboard: resource sightings, threat
  sightings, safe spot, generic k/v scratch).
- **Goal framework** (`goals/`): `Goal` base class with name + tree + utility
  score; `GoalPriority` constants (SAFETY > SURVIVAL > PRODUCTIVE > FILLER);
  stock goals `IdleGoal`, `WanderGoal`, `GatherResourceGoal`, `FleeGoal`,
  `DefendGoal`.
- **Stock nodes** (`nodes/`): `HasResourceCondition`, `HasThreatCondition`,
  `MoveToNode`, `HarvestResourceNode`, `PerceiveThreatsNode`, plus a
  `Navigator` interface so the module does not hard-bind to any movement
  back-end.
- **Systems** (`systems/`): `AIArbiter` (pure-logic tick) and
  `AIUpdateSystem` (`@RegisterSystem(AUTHORITY)` `UpdateSubscriberSystem`
  that drives all entities carrying `AIComponent` + `GoalComponent`).
- **Event** (`events/AIGoalChangedEvent`): fired when an NPC's active goal
  switches; carries previous and new goal names.
- **Tests**: 7 JUnit 5 test classes covering composites, decorators, memory
  semantics, arbitration (safety > gather > idle), and full
  interrupt-then-resume flow.

### Design notes

- Does not modify or depend on the engine's existing behavior-tree
  implementation under `engine/.../logic/behavior/`. Both systems can
  coexist; this module ships its own runtime so it remains self-contained.
- Movement and perception are injected via `Navigator` and
  `PerceiveThreatsNode`'s scan callback — game integrations supply concrete
  implementations without editing this module.
- Goal selection is utility-based: each goal returns a numeric score each
  tick; highest score wins; switches reset the outgoing tree so it starts
  cleanly on resume.
