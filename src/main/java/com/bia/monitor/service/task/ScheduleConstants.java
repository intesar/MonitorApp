/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bia.monitor.service.task;

/**
 *
 * @author Intesar Mohammed
 */
public interface ScheduleConstants {
    // SUNDAY 4 PM
    String WEEKLY_SCHEDULE = "0 15 18 * * SUN";
    // EVERY 15 MINS
    int DAILY_UPSITE_SCHEDULE = 15*60*1000;
    // EVERY 2 MIN
    int DAILY_DOWNSITE_SCHEDULE = 2*60*1000;
    // EVERY DAY 4 PM
    String DAILY_ADMIN_SCHEDULE = "0 15 18 * * ?";
}
