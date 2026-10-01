/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cdancy.bitbucket.rest.fallbacks;

import com.cdancy.bitbucket.rest.domain.common.Error;
import org.jclouds.http.HttpResponse;
import org.jclouds.http.HttpResponseException;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link BitbucketFallbacks#getErrors(Throwable)} on failures the mock server cannot produce.
 */
@Test(groups = "unit", testName = "BitbucketFallbacksMockTest")
public class BitbucketFallbacksMockTest {

    public void testFailureWithoutResponseHasNoStatus() {
        final List<Error> errors = BitbucketFallbacks.getErrors(new IOException("Read timed out"));

        assertThat(errors).hasSize(1);
        assertThat(errors.get(0).statusCode()).isNull();
    }

    public void testWrappedHttpFailureKeepsStatus() {
        final HttpResponse response = HttpResponse.builder().statusCode(429).build();
        final Throwable wrapped = new RuntimeException(new HttpResponseException("rate limited", null, response));

        final List<Error> errors = BitbucketFallbacks.getErrors(wrapped);

        assertThat(errors).hasSize(1);
        assertThat(errors.get(0).statusCode()).isEqualTo(429);
    }
}
