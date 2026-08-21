package com.example.gibdd_ochevidec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.gibdd_ochevidec.ui.screens.chat.ChatScreen
import com.example.gibdd_ochevidec.ui.screens.chat.ChatsScreen
import com.example.gibdd_ochevidec.ui.screens.common.LoadingScreen
import com.example.gibdd_ochevidec.ui.screens.common.WaitingRoleScreen
import com.example.gibdd_ochevidec.ui.theme.GIBDD_OchevidecTheme

private enum class AppScreen {
    LOADING,
    WAITING_ROLE,
    CHATS,
    CHAT
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            GIBDD_OchevidecTheme {

                var currentScreen by remember {
                    mutableStateOf(AppScreen.LOADING)
                }

                var selectedChatId by remember {
                    mutableStateOf("")
                }

                var selectedChatName by remember {
                    mutableStateOf("")
                }

                // Локальные псевдонимы
                val aliasPreferences = remember {
                    getSharedPreferences(
                        "chat_aliases",
                        MODE_PRIVATE
                    )
                }

                var aliases by remember {

                    val loadedAliases =
                        aliasPreferences.all
                            .filterKeys {
                                it.startsWith("alias_")
                            }
                            .mapKeys {
                                it.key.removePrefix("alias_")
                            }
                            .mapValues {
                                it.value.toString()
                            }

                    mutableStateOf(loadedAliases)
                }

                // Пока это локальная имитация бана.
                // Потом список будет приходить с backend.
                var hiddenChatIds by remember {
                    mutableStateOf<Set<String>>(
                        emptySet()
                    )
                }


                when (currentScreen) {

                    AppScreen.LOADING -> {

                        LoadingScreen(
                            onFinished = {
                                currentScreen =
                                    AppScreen.WAITING_ROLE
                            }
                        )
                    }


                    AppScreen.WAITING_ROLE -> {

                        WaitingRoleScreen(
                            onRoleAssigned = {
                                currentScreen =
                                    AppScreen.CHATS
                            }
                        )
                    }


                    AppScreen.CHATS -> {

                        ChatsScreen(
                            hiddenChatIds = hiddenChatIds,
                            aliases = aliases,
                            onChatClick = { id, name ->

                                selectedChatId = id
                                selectedChatName = name

                                currentScreen =
                                    AppScreen.CHAT
                            }
                        )
                    }


                    AppScreen.CHAT -> {

                        ChatScreen(
                            chatId = selectedChatId,
                            chatName = selectedChatName,

                            onBackClick = {
                                currentScreen =
                                    AppScreen.CHATS
                            },

                            onAliasChanged = { newAlias ->

                                // Сохраняем псевдоним
                                // только на этом устройстве
                                aliasPreferences
                                    .edit()
                                    .putString(
                                        "alias_$selectedChatId",
                                        newAlias
                                    )
                                    .apply()

                                aliases =
                                    aliases + (
                                            selectedChatId to newAlias
                                            )

                                selectedChatName = newAlias
                            },

                            onBlockConfirmed = {

                                // Для Инспектора чат после
                                // бана скрывается из списка
                                hiddenChatIds =
                                    hiddenChatIds +
                                            selectedChatId

                                currentScreen =
                                    AppScreen.CHATS
                            }
                        )
                    }
                }
            }
        }
    }
}