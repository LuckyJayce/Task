/**
 * Copyright 2014 Netflix, Inc.
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
package com.shizhefei.task.function;
/**
 * copy from rxjava
 */
public class Actions {

    public static <T0, T1, T2> EmptyAction<T0, T1, T2> empty() {
        return EMPTY_ACTION;
    }

    @SuppressWarnings("rawtypes")
    private static final EmptyAction EMPTY_ACTION = new EmptyAction();

    private static final class EmptyAction<T0, T1, T2> implements
            Action0,
            Action1<T0>,
            Action2<T0, T1>,
            Action3<T0, T1, T2> {
        EmptyAction() {
        }

        @Override
        public void call() {
        }

        @Override
        public void call(T0 t1) {
        }

        @Override
        public void call(T0 t1, T1 t2) {
        }

        @Override
        public void call(T0 t1, T1 t2, T2 t3) {
        }
    }
}
