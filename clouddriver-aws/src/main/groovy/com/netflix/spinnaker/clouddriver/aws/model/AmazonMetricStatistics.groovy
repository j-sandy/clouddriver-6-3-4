/*
 * Copyright 2016 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.clouddriver.aws.model

import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult
import com.fasterxml.jackson.annotation.JsonInclude
import com.netflix.spinnaker.clouddriver.model.CloudMetricStatistics
import groovy.transform.Immutable

@Immutable
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class AmazonMetricStatistics implements CloudMetricStatistics<AmazonMetricDatapoint> {

  String unit
  List<AmazonMetricDatapoint> datapoints

  static AmazonMetricStatistics from(GetMetricStatisticsResult result) {
    def datapoints = result.datapoints
        .sort { a, b -> a.timestamp <=> b.timestamp }
        .findResults {
          new AmazonMetricDatapoint(it.timestamp, it.average, it.sum, it.sampleCount, it.minimum, it.maximum)
        } as List
    def unit = result.datapoints ? result.datapoints[0].unit : null

    new AmazonMetricStatistics(unit, datapoints)
  }

}
