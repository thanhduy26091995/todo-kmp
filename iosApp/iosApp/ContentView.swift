import SwiftUI
import Shared

struct ContentView: View {
    @ObservedObject private(set) var viewModel: ViewModel
    
    var body: some View {
        NavigationView {
            listView()
                .navigationTitle("Todo List")
                .navigationBarItems(trailing:
                                        Button("Reload") {
                    self.viewModel.loadTodos(forceLoad: true)
                })
        }
    }
    
    private func listView() -> AnyView {
        switch viewModel.todos {
        case .loading:
            return AnyView(Text("..Loading").multilineTextAlignment(.center))
        case .result(let todos):
            return AnyView(List(todos) { todo in
                Text(todo.title)
            })
        case .error(let description):
            return AnyView(Text(description).multilineTextAlignment(.center))
        }
    }
}

extension TodoDTO: @retroactive Identifiable {}

extension ContentView {
    
    enum LoadableTodos {
        case loading
        case result([TodoDTO])
        case error(String)
    }
    
    @MainActor
    class ViewModel: ObservableObject {
        @Published var todos = LoadableTodos.loading
        
        let helper = KoinHelper()
        
        init() {
            loadTodos(forceLoad: true)
        }
        
        func loadTodos(forceLoad: Bool) {
            Task {
                do {
                    self.todos = .loading
                    let todoList  = try await helper.getTodos(forceReload: forceLoad)
                    self.todos = .result(todoList)
                }
                catch {
                    self.todos = .error(error.localizedDescription)
                }
            }
        }
    }
}
