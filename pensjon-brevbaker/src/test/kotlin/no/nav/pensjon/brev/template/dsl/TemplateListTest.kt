package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TemplateListTest {
    @Test
    fun `list can be created with default values`() {
        val doc = outlineTestTemplate {
            list {
                item {
                    text(Language.Bokmal to "Test")
                }
            }
        }

        val expected = outlineTestLetter(
            Element.ItemList(
                listOf(
                    Element.ItemList.Item(listOf(newText(Language.Bokmal to "Test")))
                )
            )
        )

        Assertions.assertEquals(doc, expected)
    }

    @Test
    fun `list creation fails when list has no item definitions`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            outlineTestTemplate {
                list {}
            }
        }
    }

    @Test
    fun `item conditions are added`() {
        val doc = outlineTestTemplate {
            list {
                showIf(true.expr()) {
                    item {
                        text(Language.Bokmal to "Test")
                    }
                }
            }
        }

        val expected = outlineTestLetter(
            Element.ItemList(
                listOf(
                    Element.ItemList.Item(
                        listOf(newText(Language.Bokmal to "Test")), true.expr()
                    )
                )
            )
        )

        Assertions.assertEquals(doc, expected)
    }

}