/*
 * Copyright (c) 2019 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.wikipediaroulette.wikipedia

/**
 * @author toastkidjp
 */
class HostGenerator {

    operator fun invoke(language: String) = "https://$language.wikipedia.org/"
}