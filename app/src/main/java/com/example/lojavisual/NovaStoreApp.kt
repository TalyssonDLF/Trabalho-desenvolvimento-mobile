@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.lojavisual

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

private val NovaStoreColors = lightColorScheme(
    primary = Color(0xFF5B5FEF),
    onPrimary = Color.White,
    background = Color(0xFFF6F7FB),
    surface = Color.White,
    onSurface = Color(0xFF1E2432),
    secondary = Color(0xFF1E9E62)
)

@Composable
fun NovaStoreApp() {
    MaterialTheme(colorScheme = NovaStoreColors) {
        val navController = rememberNavController()
        val produtos = remember {
            mutableStateListOf<Produto>().apply { addAll(produtosIniciais()) }
        }
        val cupons = remember {
            mutableStateListOf<Cupom>().apply { addAll(cuponsIniciais()) }
        }
        val carrinho = remember { mutableStateListOf<CarrinhoItem>() }
        var cupomAplicadoId by remember { mutableStateOf<Int?>(null) }

        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStackEntry?.destination?.route
        val mainRoutes = setOf(Rotas.PRODUTOS, Rotas.CUPONS, Rotas.CARRINHO, Rotas.SOBRE)

        fun adicionarAoCarrinho(produto: Produto, quantidade: Int) {
            val index = carrinho.indexOfFirst { it.produtoId == produto.id }
            if (index >= 0) {
                val atual = carrinho[index]
                carrinho[index] = atual.copy(
                    quantidade = (atual.quantidade + quantidade).coerceAtMost(9)
                )
            } else {
                carrinho.add(
                    CarrinhoItem(
                        produtoId = produto.id,
                        nome = produto.nome,
                        precoUnitarioCentavos = produto.precoCentavos,
                        quantidade = quantidade,
                        imageRes = produto.imageRes
                    )
                )
            }
        }

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                if (currentRoute in mainRoutes) {
                    NavigationBar {
                        NavigationBarItem(
                            selected = currentRoute == Rotas.PRODUTOS,
                            onClick = {
                                navController.navigate(Rotas.PRODUTOS) {
                                    popUpTo(Rotas.PRODUTOS) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(Icons.Default.Home, contentDescription = null) },
                            label = { Text("Loja") }
                        )
                        NavigationBarItem(
                            selected = currentRoute == Rotas.CUPONS,
                            onClick = {
                                navController.navigate(Rotas.CUPONS) {
                                    popUpTo(Rotas.PRODUTOS) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(Icons.Default.LocalOffer, contentDescription = null) },
                            label = { Text("Cupons") }
                        )
                        NavigationBarItem(
                            selected = currentRoute == Rotas.CARRINHO,
                            onClick = {
                                navController.navigate(Rotas.CARRINHO) {
                                    popUpTo(Rotas.PRODUTOS) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                            label = { Text("Carrinho") }
                        )
                        NavigationBarItem(
                            selected = currentRoute == Rotas.SOBRE,
                            onClick = {
                                navController.navigate(Rotas.SOBRE) {
                                    popUpTo(Rotas.PRODUTOS) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(Icons.Default.Info, contentDescription = null) },
                            label = { Text("Sobre") }
                        )
                    }
                }
            }
        ) { rootPadding ->
            NavHost(
                navController = navController,
                startDestination = Rotas.PRODUTOS,
                modifier = Modifier.padding(rootPadding)
            ) {
                composable(Rotas.PRODUTOS) {
                    ProdutosScreen(
                        produtos = produtos,
                        onOpenProduto = { navController.navigate(Rotas.produtoDetalhe(it)) },
                        onAddProduto = { produtos.add(it) },
                        onRemoveProduto = { produto ->
                            produtos.remove(produto)
                            carrinho.removeAll { it.produtoId == produto.id }
                        }
                    )
                }

                composable(
                    route = Rotas.PRODUTO_DETALHE,
                    arguments = listOf(navArgument("produtoId") { type = NavType.IntType })
                ) { entry ->
                    val produtoId = entry.arguments?.getInt("produtoId")
                    val produto = produtos.firstOrNull { it.id == produtoId }
                    ProdutoDetalheScreen(
                        produto = produto,
                        onBack = { navController.popBackStack() },
                        onAddToCart = { item, quantidade ->
                            adicionarAoCarrinho(item, quantidade)
                            navController.navigate(Rotas.CARRINHO)
                        }
                    )
                }

                composable(Rotas.CUPONS) {
                    CuponsScreen(
                        cupons = cupons,
                        onOpenCupom = { navController.navigate(Rotas.cupomDetalhe(it)) },
                        onAddCupom = { cupons.add(it) },
                        onRemoveCupom = { cupom ->
                            cupons.remove(cupom)
                            if (cupomAplicadoId == cupom.id) cupomAplicadoId = null
                        }
                    )
                }

                composable(
                    route = Rotas.CUPOM_DETALHE,
                    arguments = listOf(navArgument("cupomId") { type = NavType.IntType })
                ) { entry ->
                    val cupomId = entry.arguments?.getInt("cupomId")
                    val cupom = cupons.firstOrNull { it.id == cupomId }
                    val subtotal = carrinho.sumOf { it.subtotalCentavos }
                    CupomDetalheScreen(
                        cupom = cupom,
                        subtotalCarrinho = subtotal,
                        aplicado = cupom?.id == cupomAplicadoId,
                        onBack = { navController.popBackStack() },
                        onToggleAtivo = { item, ativo ->
                            val index = cupons.indexOfFirst { it.id == item.id }
                            if (index >= 0) cupons[index] = item.copy(ativo = ativo)
                            if (!ativo && cupomAplicadoId == item.id) cupomAplicadoId = null
                        },
                        onApply = { item ->
                            cupomAplicadoId = item.id
                            navController.navigate(Rotas.CARRINHO)
                        }
                    )
                }

                composable(Rotas.CARRINHO) {
                    val cupom = cupons.firstOrNull { it.id == cupomAplicadoId && it.ativo }
                    CarrinhoScreen(
                        itens = carrinho,
                        cupom = cupom,
                        onChangeQuantidade = { item, novaQuantidade ->
                            val index = carrinho.indexOfFirst { it.produtoId == item.produtoId }
                            if (index >= 0 && novaQuantidade in 1..9) {
                                carrinho[index] = item.copy(quantidade = novaQuantidade)
                            }
                        },
                        onRemove = { carrinho.remove(it) },
                        onCheckout = { navController.navigate(Rotas.PAGAMENTO) }
                    )
                }

                composable(Rotas.PAGAMENTO) {
                    val subtotal = carrinho.sumOf { it.subtotalCentavos }
                    val cupom = cupons.firstOrNull { it.id == cupomAplicadoId && it.ativo }
                    val desconto = calcularDesconto(subtotal, cupom)
                    PagamentoScreen(
                        subtotalCentavos = subtotal,
                        descontoCentavos = desconto,
                        totalCentavos = (subtotal - desconto).coerceAtLeast(0),
                        cupom = cupom,
                        onBack = { navController.popBackStack() },
                        onFinish = {
                            carrinho.clear()
                            cupomAplicadoId = null
                            navController.navigate(Rotas.PRODUTOS) {
                                popUpTo(Rotas.PRODUTOS) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    )
                }

                composable(Rotas.SOBRE) {
                    SobreScreen()
                }
            }
        }
    }
}

@Composable
private fun ProdutosScreen(
    produtos: List<Produto>,
    onOpenProduto: (Int) -> Unit,
    onAddProduto: (Produto) -> Unit,
    onRemoveProduto: (Produto) -> Unit
) {
    var busca by rememberSaveable { mutableStateOf("") }
    var nome by rememberSaveable { mutableStateOf("") }
    var preco by rememberSaveable { mutableStateOf("") }
    var categoria by rememberSaveable { mutableStateOf("") }
    var descricao by rememberSaveable { mutableStateOf("") }

    val filtrados = produtos.filter {
        busca.isBlank() || it.nome.contains(busca, ignoreCase = true) ||
            it.categoria.contains(busca, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("NOVA STORE", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
                        Text("Produtos", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                OutlinedTextField(
                    value = busca,
                    onValueChange = { busca = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Buscar produtos") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Adicionar produto", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(
                            "O item entra na lista imediatamente e permanece em memória enquanto o app estiver aberto.",
                            color = Color(0xFF6F7482),
                            fontSize = 13.sp
                        )
                        OutlinedTextField(
                            value = nome,
                            onValueChange = { nome = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Nome") },
                            singleLine = true
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = preco,
                                onValueChange = { preco = it },
                                modifier = Modifier.weight(1f),
                                label = { Text("Preço") },
                                placeholder = { Text("149,90") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = categoria,
                                onValueChange = { categoria = it },
                                modifier = Modifier.weight(1f),
                                label = { Text("Categoria") },
                                singleLine = true
                            )
                        }
                        OutlinedTextField(
                            value = descricao,
                            onValueChange = { descricao = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Descrição") },
                            minLines = 2
                        )
                        Button(
                            onClick = {
                                val precoCentavos = parsePriceToCents(preco) ?: return@Button
                                val novoId = (produtos.maxOfOrNull { it.id } ?: 0) + 1
                                onAddProduto(
                                    Produto(
                                        id = novoId,
                                        nome = nome.trim(),
                                        precoCentavos = precoCentavos,
                                        categoria = categoria.trim().ifBlank { "Geral" },
                                        descricao = descricao.trim().ifBlank { "Produto adicionado pelo usuário." },
                                        imageRes = R.drawable.ic_store
                                    )
                                )
                                nome = ""
                                preco = ""
                                categoria = ""
                                descricao = ""
                            },
                            enabled = nome.isNotBlank() && (parsePriceToCents(preco) ?: 0) > 0,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Adicionar produto")
                        }
                    }
                }
            }

            item {
                Text(
                    "${filtrados.size} produto(s)",
                    color = Color(0xFF6F7482),
                    fontSize = 13.sp
                )
            }

            items(filtrados, key = { it.id }) { produto ->
                ProdutoCard(
                    produto = produto,
                    onClick = { onOpenProduto(produto.id) },
                    onRemove = { onRemoveProduto(produto) }
                )
            }
        }
    }
}

@Composable
private fun ProdutoCard(
    produto: Produto,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(produto.imageRes),
                contentDescription = produto.nome,
                modifier = Modifier.size(88.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(produto.categoria.uppercase(), color = MaterialTheme.colorScheme.primary, fontSize = 11.sp)
                Text(
                    produto.nome,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(formatCurrency(produto.precoCentavos), fontWeight = FontWeight.Bold)
                Text("Toque para ver detalhes", color = Color(0xFF6F7482), fontSize = 12.sp)
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Remover ${produto.nome}")
            }
        }
    }
}

@Composable
private fun ProdutoDetalheScreen(
    produto: Produto?,
    onBack: () -> Unit,
    onAddToCart: (Produto, Int) -> Unit
) {
    var quantidade by remember(produto?.id) { mutableStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do produto") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (produto == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Produto não encontrado")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(produto.imageRes),
                                contentDescription = produto.nome,
                                modifier = Modifier.size(220.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(produto.categoria.uppercase(), color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
                        Text(produto.nome, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Text(formatCurrency(produto.precoCentavos), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text(produto.descricao, color = Color(0xFF6F7482))
                    }
                }
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("Quantidade", fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { if (quantidade > 1) quantidade-- },
                                    enabled = quantidade > 1
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Diminuir")
                                }
                                Text(
                                    quantidade.toString(),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                IconButton(
                                    onClick = { if (quantidade < 9) quantidade++ },
                                    enabled = quantidade < 9
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Aumentar")
                                }
                            }
                            HorizontalDivider()
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total calculado")
                                Text(
                                    formatCurrency(produto.precoCentavos * quantidade),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
                item {
                    Button(
                        onClick = { onAddToCart(produto, quantidade) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Adicionar ao carrinho")
                    }
                }
            }
        }
    }
}

@Composable
private fun CuponsScreen(
    cupons: List<Cupom>,
    onOpenCupom: (Int) -> Unit,
    onAddCupom: (Cupom) -> Unit,
    onRemoveCupom: (Cupom) -> Unit
) {
    var codigo by rememberSaveable { mutableStateOf("") }
    var percentual by rememberSaveable { mutableStateOf("") }
    var descricao by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Cupons", fontWeight = FontWeight.Bold) }) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Adicionar cupom", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        OutlinedTextField(
                            value = codigo,
                            onValueChange = { codigo = it.uppercase() },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Código") },
                            placeholder = { Text("OFERTA10") },
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = percentual,
                            onValueChange = { percentual = it.filter(Char::isDigit).take(2) },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Desconto (%)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = descricao,
                            onValueChange = { descricao = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Descrição") },
                            minLines = 2
                        )
                        val desconto = percentual.toIntOrNull() ?: 0
                        Button(
                            onClick = {
                                val novoId = (cupons.maxOfOrNull { it.id } ?: 0) + 1
                                onAddCupom(
                                    Cupom(
                                        id = novoId,
                                        codigo = codigo.trim().uppercase(),
                                        percentual = desconto,
                                        descricao = descricao.trim().ifBlank { "Cupom criado pelo usuário." }
                                    )
                                )
                                codigo = ""
                                percentual = ""
                                descricao = ""
                            },
                            enabled = codigo.isNotBlank() && desconto in 1..99,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Adicionar cupom")
                        }
                    }
                }
            }

            items(cupons, key = { it.id }) { cupom ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenCupom(cupom.id) },
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.LocalOffer,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(cupom.codigo, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text("${cupom.percentual}% de desconto")
                            Text(
                                if (cupom.ativo) "Ativo" else "Inativo",
                                color = if (cupom.ativo) Color(0xFF1E9E62) else Color(0xFF6F7482),
                                fontSize = 12.sp
                            )
                        }
                        IconButton(onClick = { onRemoveCupom(cupom) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Remover ${cupom.codigo}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CupomDetalheScreen(
    cupom: Cupom?,
    subtotalCarrinho: Int,
    aplicado: Boolean,
    onBack: () -> Unit,
    onToggleAtivo: (Cupom, Boolean) -> Unit,
    onApply: (Cupom) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do cupom") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (cupom == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Cupom não encontrado")
            }
        } else {
            val economia = calcularDesconto(subtotalCarrinho, cupom)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp)) {
                    Column(
                        modifier = Modifier.padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            Icons.Default.LocalOffer,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(cupom.codigo, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Text("${cupom.percentual}% de desconto", fontSize = 22.sp, color = MaterialTheme.colorScheme.primary)
                        Text(cupom.descricao, color = Color(0xFF6F7482))
                        HorizontalDivider()
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Cupom ativo", fontWeight = FontWeight.Bold)
                                Text("Pode ser aplicado no carrinho", fontSize = 12.sp, color = Color(0xFF6F7482))
                            }
                            Switch(
                                checked = cupom.ativo,
                                onCheckedChange = { onToggleAtivo(cupom, it) }
                            )
                        }
                    }
                }

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Simulação no carrinho", fontWeight = FontWeight.Bold)
                        Text("Subtotal: ${formatCurrency(subtotalCarrinho)}")
                        Text("Economia: ${formatCurrency(economia)}")
                        Text(
                            if (subtotalCarrinho == 0) "Adicione produtos ao carrinho para aplicar o cupom."
                            else "Total com cupom: ${formatCurrency((subtotalCarrinho - economia).coerceAtLeast(0))}",
                            color = Color(0xFF6F7482)
                        )
                    }
                }

                Button(
                    onClick = { onApply(cupom) },
                    enabled = cupom.ativo && subtotalCarrinho > 0,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (aplicado) "Cupom aplicado" else "Aplicar ao carrinho")
                }
            }
        }
    }
}

@Composable
private fun CarrinhoScreen(
    itens: List<CarrinhoItem>,
    cupom: Cupom?,
    onChangeQuantidade: (CarrinhoItem, Int) -> Unit,
    onRemove: (CarrinhoItem) -> Unit,
    onCheckout: () -> Unit
) {
    val subtotal = itens.sumOf { it.subtotalCentavos }
    val desconto = calcularDesconto(subtotal, cupom)
    val total = (subtotal - desconto).coerceAtLeast(0)

    Scaffold(
        topBar = { TopAppBar(title = { Text("Carrinho", fontWeight = FontWeight.Bold) }) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (itens.isEmpty()) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(48.dp))
                            Text("Seu carrinho está vazio", fontWeight = FontWeight.Bold)
                            Text("Abra um produto e adicione uma quantidade.", color = Color(0xFF6F7482))
                        }
                    }
                }
            } else {
                items(itens, key = { it.produtoId }) { item ->
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(item.imageRes),
                                contentDescription = item.nome,
                                modifier = Modifier.size(72.dp),
                                contentScale = ContentScale.Fit
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.nome, fontWeight = FontWeight.Bold)
                                Text(formatCurrency(item.subtotalCentavos), color = MaterialTheme.colorScheme.primary)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { onChangeQuantidade(item, item.quantidade - 1) },
                                        enabled = item.quantidade > 1
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "Diminuir")
                                    }
                                    Text(item.quantidade.toString(), fontWeight = FontWeight.Bold)
                                    IconButton(
                                        onClick = { onChangeQuantidade(item, item.quantidade + 1) },
                                        enabled = item.quantidade < 9
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Aumentar")
                                    }
                                }
                            }
                            IconButton(onClick = { onRemove(item) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Remover ${item.nome}")
                            }
                        }
                    }
                }

                item {
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            SummaryRow("Subtotal", formatCurrency(subtotal))
                            if (cupom != null) {
                                SummaryRow("Cupom ${cupom.codigo}", "-${formatCurrency(desconto)}")
                            }
                            HorizontalDivider()
                            SummaryRow("Total", formatCurrency(total), destaque = true)
                            Button(
                                onClick = onCheckout,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                            ) {
                                Text("Ir para pagamento")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String, destaque: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = if (destaque) FontWeight.Bold else FontWeight.Normal)
        Text(
            value,
            fontWeight = FontWeight.Bold,
            color = if (destaque) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun PagamentoScreen(
    subtotalCentavos: Int,
    descontoCentavos: Int,
    totalCentavos: Int,
    cupom: Cupom?,
    onBack: () -> Unit,
    onFinish: () -> Unit
) {
    var metodo by rememberSaveable { mutableStateOf("Cartão") }
    var nomeCartao by rememberSaveable { mutableStateOf("") }
    var numeroCartao by rememberSaveable { mutableStateOf("") }
    var validade by rememberSaveable { mutableStateOf("") }
    var mostrarSucesso by remember { mutableStateOf(false) }

    val cartaoValido = nomeCartao.isNotBlank() && numeroCartao.filter(Char::isDigit).length >= 12 && validade.isNotBlank()
    val podeFinalizar = totalCentavos > 0 && (metodo == "PIX" || cartaoValido)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pagamento") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Resumo", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        SummaryRow("Subtotal", formatCurrency(subtotalCentavos))
                        if (cupom != null) SummaryRow("Desconto ${cupom.codigo}", "-${formatCurrency(descontoCentavos)}")
                        HorizontalDivider()
                        SummaryRow("Total", formatCurrency(totalCentavos), destaque = true)
                    }
                }
            }

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Forma de pagamento", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = metodo == "Cartão", onClick = { metodo = "Cartão" })
                            Text("Cartão", modifier = Modifier.clickable { metodo = "Cartão" })
                            Spacer(Modifier.width(20.dp))
                            RadioButton(selected = metodo == "PIX", onClick = { metodo = "PIX" })
                            Text("PIX", modifier = Modifier.clickable { metodo = "PIX" })
                        }

                        if (metodo == "Cartão") {
                            OutlinedTextField(
                                value = nomeCartao,
                                onValueChange = { nomeCartao = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Nome no cartão") },
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = numeroCartao,
                                onValueChange = { numeroCartao = it.filter(Char::isDigit).take(16) },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Número do cartão") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = validade,
                                onValueChange = { validade = it.take(5) },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Validade (MM/AA)") },
                                singleLine = true
                            )
                        } else {
                            Text(
                                "PIX demonstrativo selecionado. Nenhuma cobrança real será realizada.",
                                color = Color(0xFF6F7482)
                            )
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = { mostrarSucesso = true },
                    enabled = podeFinalizar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Text("Finalizar compra")
                }
            }
        }
    }

    if (mostrarSucesso) {
        AlertDialog(
            onDismissRequest = { mostrarSucesso = false },
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = null) },
            title = { Text("Pagamento aprovado!") },
            text = {
                Text("Compra demonstrativa concluída com sucesso. Total: ${formatCurrency(totalCentavos)}")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarSucesso = false
                        onFinish()
                    }
                ) {
                    Text("Voltar à loja")
                }
            }
        )
    }
}

@Composable
private fun SobreScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Sobre o projeto", fontWeight = FontWeight.Bold) }) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Nova Store", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Text("Trabalho 2 — MAF (Mínimo Aplicativo Funcional)")
                        Text("Versão 2.0", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("O que o app demonstra", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("• Navegação centralizada com NavHost e NavController")
                        Text("• BottomNavigation funcional entre as áreas principais")
                        Text("• Lista de Produtos com adicionar, remover e abrir detalhes")
                        Text("• Lista de Cupons com adicionar, remover e abrir detalhes")
                        Text("• Carrinho com alteração de quantidade e cálculo de total")
                        Text("• Pagamento visual com Cartão ou PIX")
                        Text("• Dados mantidos somente em memória")
                    }
                }
            }
            item {
                Text(
                    "Projeto acadêmico demonstrativo. Não existe backend, banco de dados ou pagamento real.",
                    color = Color(0xFF6F7482),
                    fontSize = 13.sp
                )
            }
        }
    }
}

private fun produtosIniciais() = listOf(
    Produto(
        id = 1,
        nome = "Headphone Pro",
        precoCentavos = 14990,
        categoria = "Áudio",
        descricao = "Headphone sem fio com design confortável, acabamento premium e bateria para acompanhar música, estudos e jogos durante todo o dia.",
        imageRes = R.drawable.product_headphone
    ),
    Produto(
        id = 2,
        nome = "Smartwatch Neo",
        precoCentavos = 19990,
        categoria = "Tecnologia",
        descricao = "Smartwatch leve e moderno com mostrador digital, acompanhamento da rotina e visual versátil para usar em qualquer ocasião.",
        imageRes = R.drawable.product_watch
    ),
    Produto(
        id = 3,
        nome = "Tênis Urban",
        precoCentavos = 29990,
        categoria = "Moda",
        descricao = "Tênis casual com solado confortável e estilo urbano, ideal para o dia a dia e combinações modernas.",
        imageRes = R.drawable.product_sneaker
    ),
    Produto(
        id = 4,
        nome = "Teclado Mini",
        precoCentavos = 18990,
        categoria = "Acessórios",
        descricao = "Teclado compacto para produtividade e jogos, com visual minimalista e tamanho perfeito para mesas menores.",
        imageRes = R.drawable.product_keyboard
    )
)

private fun cuponsIniciais() = listOf(
    Cupom(1, "BEMVINDO10", 10, "Desconto de boas-vindas para qualquer produto da loja."),
    Cupom(2, "TECH15", 15, "Cupom promocional para uma compra demonstrativa de tecnologia."),
    Cupom(3, "NOVA20", 20, "Oferta especial criada para demonstrar a segunda lista do MAF.")
)

private fun calcularDesconto(subtotalCentavos: Int, cupom: Cupom?): Int {
    if (cupom == null || !cupom.ativo) return 0
    return (subtotalCentavos * (cupom.percentual / 100.0)).roundToInt()
}

private fun parsePriceToCents(value: String): Int? {
    val clean = value
        .replace("R$", "", ignoreCase = true)
        .trim()
    if (clean.isBlank()) return null

    val normalized = if (clean.contains(',')) {
        clean.replace(".", "").replace(',', '.')
    } else {
        clean
    }
    val number = normalized.toDoubleOrNull() ?: return null
    if (number <= 0) return null
    return (number * 100).roundToInt()
}

private fun formatCurrency(cents: Int): String {
    return NumberFormat
        .getCurrencyInstance(Locale("pt", "BR"))
        .format(cents / 100.0)
}
