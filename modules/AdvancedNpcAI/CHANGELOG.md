# Changelog

## [1.0.0] - 2026-04-17

### Added

#### Components
- **AIComponent** - Main configuration component for NPC AI: detection range, health flee threshold, reevaluation interval, threat level tracking
- **GoalComponent** - Priority-sorted goal queue with configurable max goals and active goal retrieval
- **MemoryComponent** - Spatial memory system with time-based expiration for tracking known resource locations and threats
- **NpcTypeComponent** - NPC role identification for behavior selection

#### Systems
- **AISystem** - Lifecycle management: auto-provisions GoalComponent/MemoryComponent on AIComponent addition, syncs goal and threat state to the behavior tree blackboard each reevaluation interval
- **GoalSystem** - Goal addition with priority sorting, completion with event dispatch, automatic idle goal injection when queue empties, completed goal pruning
- **MemorySystem** - Memory recording with key-based deduplication, recall by key prefix, automatic stale entry eviction, ResourceFoundEvent handler
- **ThreatSystem** - Periodic threat scanning based on entity proximity and detection range, distance-based threat level assessment (LOW/MEDIUM/HIGH), automatic flee goal injection on high threat, threat cleared cleanup

#### Behavior Actions (17 total)
- **Conditions**: `is_threatened`, `has_resource_nearby`, `has_goal`, `is_health_low`, `is_at_target`, `has_resource_in_inventory`
- **Movement**: `move_to_target`, `flee_from_threat`, `wander`
- **Resources**: `find_nearest_resource`, `gather_resource`, `store_resource`
- **Goals**: `select_highest_priority_goal`, `complete_goal`, `set_goal_target`
- **Combat**: `defend`, `find_safe_location`

#### Behavior Trees
- **npcSurvival** - Top-level dynamic selector tree: prioritizes threat reaction over goal-based behavior, falls back to idle
- **resourceGatherer** - Subtree: find resource, navigate, gather, complete goal
- **threatReaction** - Subtree: flee if health low, otherwise defend
- **idle** - Subtree: wander randomly then wait

#### Events
- **ThreatDetectedEvent** - Fired when a new threat is detected within range
- **ThreatClearedEvent** - Fired when all threats leave detection range
- **GoalChangedEvent** - Fired when the active goal changes
- **GoalCompletedEvent** - Fired when a goal is marked complete
- **ResourceFoundEvent** - Fired when a resource block is located
- **NpcSpawnedEvent** - Fired when a new NPC entity is initialized

#### Prefabs
- **gathererNpc** - Resource-gathering NPC with 50 HP, 20-block detection range
- **guardNpc** - Defensive NPC with 100 HP, 30-block detection range, faster reevaluation

#### Tests
- Unit tests for all component copyFrom() methods and default values
- Integration tests for GoalSystem (priority sorting, completion, eviction, idle fallback)
- Integration tests for MemorySystem (remember/recall, key deduplication, max entries eviction)
- Integration tests for AISystem (auto-provisioning, NpcSpawnedEvent)
- Integration tests for ThreatSystem (threat detection within/outside range)
- Action tests for MoveToTargetAction and GatherResourceAction
- Full lifecycle integration test
