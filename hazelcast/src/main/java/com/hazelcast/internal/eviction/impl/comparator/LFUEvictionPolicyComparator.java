/*
 * Copyright (c) 2008-2019, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.internal.eviction.impl.comparator;

import com.hazelcast.internal.eviction.EvictableEntryView;
import com.hazelcast.internal.eviction.EvictionPolicyComparator;
import com.hazelcast.nio.serialization.SerializableByConvention;

/**
 * {@link com.hazelcast.config.EvictionPolicy#LFU} policy based {@link EvictionPolicyComparator}.
 */
@SerializableByConvention
public class LFUEvictionPolicyComparator extends EvictionPolicyComparator {

    @Override
    public int compare(EvictableEntryView e1, EvictableEntryView e2) {
        long hits1 = e1.getAccessHit();
        long hits2 = e2.getAccessHit();
        if (hits2 < hits1) {
            return SECOND_ENTRY_HAS_HIGHER_PRIORITY_TO_BE_EVICTED;
        } else if (hits1 < hits2) {
            return FIRST_ENTRY_HAS_HIGHER_PRIORITY_TO_BE_EVICTED;
        } else {
            long creationTime1 = e1.getCreationTime();
            long creationTime2 = e2.getCreationTime();
            // if hits are same, we select the oldest entry to evict
            if (creationTime2 < creationTime1) {
                return SECOND_ENTRY_HAS_HIGHER_PRIORITY_TO_BE_EVICTED;
            } else if (creationTime2 > creationTime1) {
                return FIRST_ENTRY_HAS_HIGHER_PRIORITY_TO_BE_EVICTED;
            } else {
                return BOTH_OF_ENTRIES_HAVE_SAME_PRIORITY_TO_BE_EVICTED;
            }
        }
    }
}
