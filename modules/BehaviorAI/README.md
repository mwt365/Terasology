# BehaviorAI

Behavior-tree-driven AI framework for Terasology NPCs.

## What it provides

- A self-contained behavior-tree runtime (Selector, Sequence, Parallel,
  Inverter, Repeat, Condition, Action).
- ECS components (`AIComponent`, `GoalComponent`, `MemoryComponent`)
  designed so entities opt into the parts they need.
- A utility-based goal arbiter that picks the highest-scoring goal each
  tick and ticks its tree, resetting the outgoing tree when a higher-
  priority goal preempts it so behaviors resume cleanly afterward.
- Stock goals: `Idle`, `Wander`, `GatherResource`, `Flee`, `Defend`.

## Example: a survivor NPC

```java
EntityRef npc = entityManager.create();
npc.addComponent(new AIComponent());
npc.addComponent(new MemoryComponent());

GoalComponent goals = new GoalComponent()
        .addGoal(new IdleGoal())
        .addGoal(new WanderGoal())
        .addGoal(new GatherResourceGoal(myNavigator))
        .addGoal(new FleeGoal(myNavigator));
npc.addComponent(goals);
```

`AIUpdateSystem` will tick this NPC every frame. When `MemoryComponent`
contains a threat, `FleeGoal` scores `SAFETY` (100) and preempts gathering
(`PRODUCTIVE`, 30). When threats clear, gather resumes.

## Extending

- **New goal**: extend `Goal`. Build your tree from the primitives in the
  constructor, implement `score()` to decide when you should run.
- **New action**: extend `ActionNode` and return SUCCESS / FAILURE /
  RUNNING from `tick`. Override `reset()` if you hold per-invocation state.
- **New condition**: extend `ConditionNode`, implement `evaluate()`.
- **Custom navigator**: implement `Navigator` and inject it where goals ask
  for one. The module ships with no default so it stays decoupled from
  pathfinding.

## Relationship to the engine behavior tree

The engine ships its own BT implementation in
`engine/src/main/java/org/terasology/engine/logic/behavior/`, which is
coupled to the behavior-asset format and NUI editor. This module does not
modify or depend on that implementation; both can coexist. The trade-off is
that you cannot currently load `.behavior` JSON assets into this module —
trees are constructed in code. Asset-format support can be added later
without breaking this module's API.

## Tests

Unit tests for the runtime and arbiter do not require
`@IntegrationEnvironment` and should run fast. Run the full suite with:

```
./gradlew :modules:BehaviorAI:test
```
